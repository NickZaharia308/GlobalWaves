package commands.users;

import commands.Command;
import commands.statistics.Wrapped;
import commands.users.playlists.CreatePlaylist;
import lombok.Getter;
import lombok.Setter;
import main.Library;
import user.entities.Artist;
import user.entities.Users;
import user.entities.audio.files.Playlists;
import user.entities.audio.files.Songs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * UpdateRecommendations class is used to update the recommendations for a user.
 * The recommendations can be a song, a playlist or a random playlist.
 */
@Getter
@Setter
public class UpdateRecommendations extends Command {
    private String message;
    private final int two = 2;
    private final int three = 3;
    private final int five = 5;
    private final int first = 1;
    private final int second = 2;
    private final int maxListenedThreshold = 30;

    /**
     * Updates the recommendations for a user.
     * @param command the command to be executed
     * @param library the main library
     */
    public void returnUpdateRecommendations(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());

        if (user == null) {
            setMessage("The username " + command.getUsername() + " doesn't exist.");
            return;
        }

        if (user.getUserType() != Users.UserType.NORMAL) {
            setMessage(command.getUsername() + " is not a normal user.");
            return;
        }

        // Updating the currentSong
        Wrapped wrapped = new Wrapped();
        wrapped.returnWrapped(command, library);

        if (command.getRecommendationType().equals("fans_playlist")) {
            // Getting top Songs and the name of the artist
            List<Songs> topSongs = fansPlaylist(library, user);
            String artistName = user.getMusicPlayer().getSong().getArtist();
            command.setPlaylistName(artistName + " Fan Club recommendations");

            // Creating the playlist
            CreatePlaylist createPlaylist = new CreatePlaylist();
            createPlaylist.returnCreatePlaylist(command, library);

            // Adding the playlist to user's recommended playlists and add the list of songs to it
            Playlists newlyCreatedPlaylist =
                    library.getPlaylists().get(library.getPlaylists().size() - 1);
            newlyCreatedPlaylist.setSongs((ArrayList<Songs>) topSongs);
            user.getRecommendedPlaylists().add(newlyCreatedPlaylist);
            user.setLastRecommendedPlaylist(newlyCreatedPlaylist);
            user.setLastRecommendationType(Users.Track.PLAYLIST);

            setMessage("The recommendations for user " + user.getUsername()
                    + " have been updated successfully.");
        } else if (command.getRecommendationType().equals("random_song")) {
            Songs currentSong = user.getMusicPlayer().getSong();
            int seed = currentSong.getDuration() - user.getMusicPlayer().getRemainedTime();
            if (seed < maxListenedThreshold) {
                setMessage("No recommendations available.");
                return;
            }

            // Getting the genre of the current song and get all the songs with the same genre
            String genre = currentSong.getGenre();
            List<Songs> songsWithSameGenre =
            library.getSongs().stream().
                    filter(song -> song.getGenre().equals(genre)).collect(Collectors.toList());

            // Generating a random song from the list
            Random random = new Random(seed);
            Songs randomSong = songsWithSameGenre.get(random.nextInt(songsWithSameGenre.size()));

            // Adding the song to the user's recommended songs
            user.getRecommendedSongs().add(randomSong);
            user.setLastRecommendedSong(randomSong);
            user.setLastRecommendationType(Users.Track.SONG);
            setMessage("The recommendations for user " + user.getUsername()
                    + " have been updated successfully.");
        } else if (command.getRecommendationType().equals("random_playlist")) {
            ArrayList<Songs> songsForTop3Genres = getSongsForTop3Genres(user);
            command.setPlaylistName(user.getUsername() + "'s recommendations");

            // Creating the playlist
            CreatePlaylist createPlaylist = new CreatePlaylist();
            createPlaylist.returnCreatePlaylist(command, library);

            // Adding the playlist to user's recommended playlists and add the list of songs to it
            Playlists newlyCreatedPlaylist =
                    library.getPlaylists().get(library.getPlaylists().size() - 1);
            newlyCreatedPlaylist.setSongs(songsForTop3Genres);
            user.getRecommendedPlaylists().add(newlyCreatedPlaylist);
            user.setLastRecommendedPlaylist(newlyCreatedPlaylist);
            user.setLastRecommendationType(Users.Track.PLAYLIST);

            setMessage("The recommendations for user " + user.getUsername()
                    + " have been updated successfully.");
        }

    }

    /**
     * Gets the top songs for the fans playlist.
     * @param library the main library
     * @param user the user for which the playlist is created
     * @return the list of top songs
     */
    private List<Songs> fansPlaylist(final Library library, final Users user) {
        Songs loadedSong = user.getMusicPlayer().getSong();
        Artist artist = (Artist) user.getUser(library.getUsers(), loadedSong.getArtist());

        List<String> topFans = getTopFans(artist);
        List<Songs> topSongs = new ArrayList<>();

        for (String fan : topFans) {
            Users fanUser = user.getUser(library.getUsers(), fan);
            List<Songs> fanTopSongs = getFanTopSongs(fanUser);
            topSongs.addAll(fanTopSongs);
        }
        return topSongs;
    }

    /**
     * Gets the top 5 fans for an artist.
     * @param artist the artist for which the top fans are computed
     * @return the list of top fans
     */
    private List<String> getTopFans(final Artist artist) {

        List<Map.Entry<String, Integer>> topFansList =
                new ArrayList<>(artist.getTopFans().entrySet());
        topFansList.sort(Comparator.comparingInt(Map.Entry::getValue));
        Collections.reverse(topFansList);

        List<String> topFans = new ArrayList<>();
        for (int i = 0; i < Math.min(topFansList.size(), five); i++) {
            topFans.add(topFansList.get(i).getKey());
        }

        return topFans;
    }

    /**
     * Gets the top 5 songs for a fan.
     * @param user the user for which the top songs are computed
     * @return the list of top songs
     */
    private List<Songs> getFanTopSongs(final Users user) {
        List<Songs> likedSongs = user.getLikedSongs();

        likedSongs.sort(Comparator.comparingLong(Songs::getNumberOfLikes).reversed());

        List<Songs> top5LikedSongs = likedSongs.subList(0, Math.min(likedSongs.size(), five));
        return top5LikedSongs;
    }

    /**
     * Gets the top 3 genres for a user.
     * @param user the user for which the top genres are computed
     * @return the list of top genres
     */
    private List<String> getTop3Geners(final Users user) {
        Map<String, Integer> genres = new HashMap<>();

        // For liked songs
        for (Songs song : user.getLikedSongs()) {
            String genre = song.getGenre();
            if (genres.containsKey(genre)) {
                genres.put(genre, genres.get(genre) + 1);
            } else {
                genres.put(genre, 1);
            }
        }

        // For followed playlists
        for (Playlists playlist : user.getFollowedPlaylists()) {
            for (Songs song : playlist.getSongs()) {
                String genre = song.getGenre();
                if (genres.containsKey(genre)) {
                    genres.put(genre, genres.get(genre) + 1);
                } else {
                    genres.put(genre, 1);
                }
            }
        }

        // For playlists created by the user
        for (Playlists playlist : Library.getInstance().getPlaylists()) {
            if (playlist.getOwner().equals(user.getUsername())) {
                for (Songs song : playlist.getSongs()) {
                    String genre = song.getGenre();
                    if (genres.containsKey(genre)) {
                        genres.put(genre, genres.get(genre) + 1);
                    } else {
                        genres.put(genre, 1);
                    }
                }
            }
        }

        // Get top 3 genres
        List<Map.Entry<String, Integer>> topGenresList = new ArrayList<>(genres.entrySet());
        topGenresList.sort(Comparator.comparingInt(Map.Entry::getValue));
        Collections.reverse(topGenresList);

        List<String> topGenres = new ArrayList<>();
        for (int i = 0; i < Math.min(topGenresList.size(), three); i++) {
            topGenres.add(topGenresList.get(i).getKey());
        }

        return topGenres;
    }

    /**
     * Gets the songs for the top 3 genres of a user.
     * @param user the user for which the songs are computed
     * @return the list of songs
     */
    private ArrayList<Songs> getSongsForTop3Genres(final Users user) {
        List<String> top3Genres = getTop3Geners(user);
        List<Songs> songs = new ArrayList<>();

        int indexOfGenre = 1;

        for (String genre : top3Genres) {
            List<Songs> songsWithSameGenre = Library.getInstance().getSongs().stream().
                    filter(song -> song.getGenre().equals(genre)).collect(Collectors.toList());
            songsWithSameGenre.sort(Comparator.comparingLong(Songs::getNumberOfLikes).reversed());

            // Depending on the genre rank put the songs in the list
            if (indexOfGenre == first) {
                songs.addAll(songsWithSameGenre.subList(0, Math.min(songsWithSameGenre.size(),
                        five)));
            } else if (indexOfGenre == second) {
                songs.addAll(songsWithSameGenre.subList(0, Math.min(songsWithSameGenre.size(),
                        three)));
            } else {
                songs.addAll(songsWithSameGenre.subList(0, Math.min(songsWithSameGenre.size(),
                        two)));
            }
            indexOfGenre++;
        }

        ArrayList<Songs> songNames = new ArrayList<>();
        for (Songs song : songs) {
            songNames.add(song);
        }

        return songNames;
    }

}


