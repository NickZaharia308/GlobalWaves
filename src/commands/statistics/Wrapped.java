package commands.statistics;

import commands.Command;
import commands.searchBar.Load;
import commands.users.Status;
import lombok.Getter;
import lombok.Setter;
import main.Library;
import user.entities.Artist;
import user.entities.Host;
import user.entities.Users;
import user.entities.audio.files.Episodes;
import user.entities.audio.files.Songs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**  Wrapped is a statistic that provides users with a personalized summary of their musical
 * activity, including their top-listened songs, preferred genres, and other relevant statistics,
 * throughout the runtime of the program.
 *
 */
@Getter
@Setter
public class Wrapped extends Command {
    private String message;
    private final int maxTop = 5;
    private List<Map.Entry<String, Integer>> topArtists;
    private List<Map.Entry<String, Integer>> topGenres;
    private List<Map.Entry<String, Integer>> topSongs;
    private List<Map.Entry<String, Integer>> topAlbums;
    private List<Map.Entry<String, Integer>> topEpisodes;
    private List<Map.Entry<String, Integer>> topFans;
    private List<Map.Entry<String, Boolean>> listeners;
    private Users.UserType userType = Users.UserType.NORMAL;


    /**
     * Method that returns the personalized summary of the user's musical activity.
     * It updates the maps for all normal users of the platform and then returns the
     * top-listened songs, preferred genres, and other relevant statistics.
     *
     * @param command The command containing information about the operation.
     * @param library The library containing all user entities.
     */
    public void returnWrapped(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());

        // If the user is an artist, update the maps for all normal users of the platform
        if (user.getUserType() == Users.UserType.ARTIST) {
            userType = Users.UserType.ARTIST;
            Artist artist = (Artist) user;

            if (!artist.hasTrueValueInListeners()) {
                setMessage("No data to show for artist " + artist.getUsername() + ".");
                return;
            }

            // update the maps for all normal users of the platform
            for (Users currentUser : library.getUsers()) {
                if (currentUser.getUserType() == Users.UserType.NORMAL) {
                    updateMapsForUsers(command, library, currentUser);
                }
            }

            topSongs = new ArrayList<>(artist.getTopSongs().entrySet());
            topAlbums = new ArrayList<>(artist.getTopAlbums().entrySet());
            topFans = new ArrayList<>(artist.getTopFans().entrySet());
            listeners = new ArrayList<>(artist.getListeners().entrySet());

            sortList(topSongs);
            sortList(topAlbums);
            sortList(topFans);
            return;
        }

        // If the user is a host, update the maps for all normal users of the platform
        if (user.getUserType() == Users.UserType.HOST) {
            userType = Users.UserType.HOST;
            Host host = (Host) user;

            if (!host.hasTrueValueInListeners()) {
                setMessage("No data to show for host " + host.getUsername() + ".");
                return;
            }

            // update the maps for all normal users of the platform
            for (Users currentUser : library.getUsers()) {
                if (currentUser.getUserType() == Users.UserType.NORMAL) {
                    updateMapsForUsers(command, library, currentUser);
                }
            }

            topEpisodes = new ArrayList<>(host.getTopEpisodes().entrySet());
            listeners = new ArrayList<>(host.getListeners().entrySet());

            sortList(topEpisodes);
            return;
        }
        updateMapsForUsers(command, library, user);

        if (user.getTopSongs().isEmpty() && user.getTopEpisodes().isEmpty()) {
            setMessage("No data to show for user " + user.getUsername() + ".");
            return;
        }

        topArtists = new ArrayList<>(user.getTopArtists().entrySet());
        topGenres = new ArrayList<>(user.getTopGenres().entrySet());
        topSongs = new ArrayList<>(user.getTopSongs().entrySet());
        topAlbums = new ArrayList<>(user.getTopAlbums().entrySet());
        topEpisodes = new ArrayList<>(user.getTopEpisodes().entrySet());

        sortList(topArtists);
        sortList(topGenres);
        sortList(topSongs);
        sortList(topAlbums);
        sortList(topEpisodes);

    }

    /**
     * Method to sort the list of top-listened songs, preferred genres, and other relevant
     * statistics.
     *
     * @param entryList The list of top-listened songs, preferred genres, and other relevant
     *                  statistics.
     */
    private void sortList(final List<Map.Entry<String, Integer>> entryList) {
        // Sort the list by values in descending order, and then by keys in lexicographic order
        entryList.sort(Map.Entry.<String, Integer>comparingByValue().reversed()
                .thenComparing(Map.Entry.comparingByKey()));

        // Get first 5 results
        entryList.subList(Math.min(entryList.size(), maxTop), entryList.size()).clear();
    }

    /**
     * Method to update the maps for all normal users of the platform.
     *
     * @param command The command containing information about the operation.
     * @param library The library containing all user entities.
     * @param user The user for which the maps are updated.
     */
    private void updateMapsForUsers(final Command command, final Library library,
                                    final Users user) {
        Load load = new Load();
        if (user.getMusicPlayer() == null) {
            return;
        }
        Songs oldSong = user.getMusicPlayer().getSong();
        Episodes oldEpisode = user.getMusicPlayer().getEpisode();

        command.setUsername(user.getUsername());
        Status status = new Status();
        status.returnStatus(command, library);

        /* Update the maps for the user */
        if (user.getTrackType() == Users.Track.ALBUM) {

            // if we haven't finish the album
            if (user.isSomethingLoaded()) {
                Songs currentSong = user.getMusicPlayer().getSong();
                ArrayList<Songs> songsToBeUpdated =
                        new ArrayList<>(getSongsBetween(user.getMusicPlayer().getAlbum().getSongs(),
                                oldSong, currentSong));

                for (Songs song : songsToBeUpdated) {
                    load.updateMaps(song, user, library);
                }
            } else {

                if (user.getMusicPlayer().getAlbum() == null) {
                    return;
                }
                Songs lastSong = user.getMusicPlayer().getAlbum().getSongs()
                        .get(user.getMusicPlayer().getAlbum().getSongs().size() - 1);

                ArrayList<Songs> songsToBeUpdated =
                        new ArrayList<>(getSongsBetween(user.getMusicPlayer().getAlbum().getSongs(),
                                oldSong, lastSong));

                for (Songs song : songsToBeUpdated) {
                    load.updateMaps(song, user, library);
                }
            }
        } else if (user.getTrackType() == Users.Track.PLAYLIST) {
            Songs currentSong = user.getMusicPlayer().getSong();
            load.updateMaps(currentSong, user, library);

            for (Songs song : user.getMusicPlayer().getPlaylist().getSongs()) {
                // if we reached current song break
                // else if it is the first song, don't add
                if (song.getName().equals(currentSong.getName())) {
                    break;
                } else if (song != user.getMusicPlayer().getPlaylist().getSongs().get(0)) {
                    // add it to the maps
                    load.updateMaps(song, user, library);
                }
            }
        } else if (user.getTrackType() == Users.Track.PODCAST) {
            Episodes currentEpisode = user.getMusicPlayer().getEpisode();
            ArrayList<Episodes> episodesToBeUpdated =
                    new ArrayList<>(getEpisodesBetween(user.getMusicPlayer().getPodcast()
                            .getEpisodes(), oldEpisode, currentEpisode));

            for (Episodes episode : episodesToBeUpdated) {
                load.updateTopEpisodes(episode, user,
                        user.getMusicPlayer().getPodcast().getOwner());
            }

        }
    }

    /**
     * Method to get songs between two given songs.
     *
     * @param songList The list of songs.
     * @param song1 The first song.
     * @param song2 The second song.
     * @return A list containing all songs between song1 and song2.
     */
    private static List<Songs> getSongsBetween(final List<Songs> songList, final Songs song1,
                                               final Songs song2) {
        int startIndex = songList.indexOf(song1);
        int endIndex = songList.indexOf(song2);

        if (startIndex >= 0 && endIndex >= 0 && startIndex < endIndex) {
            // Create a new list and add all songs between startIndex + 1 and endIndex
            return new ArrayList<>(songList.subList(startIndex + 1, endIndex + 1));
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Method to get episodes between two given episodes.
     *
     * @param episodeList The list of episodes.
     * @param episode1 The first episode.
     * @param episode2 The second episode.
     * @return A list containing all episodes between episode1 and episode2.
     */
    private static List<Episodes> getEpisodesBetween(final List<Episodes> episodeList,
                                                     final Episodes episode1,
                                                     final Episodes episode2) {
        int startIndex = episodeList.indexOf(episode1);
        int endIndex = episodeList.indexOf(episode2);

        if (startIndex >= 0 && endIndex >= 0 && startIndex < endIndex) {
            // Create a new list and add all episodes between startIndex + 1 and endIndex
            return new ArrayList<>(episodeList.subList(startIndex + 1, endIndex + 1));
        } else {
            return new ArrayList<>();
        }
    }
}
