package commands.statistics;

import commands.Command;
import commands.searchBar.Load;
import commands.users.Status;
import lombok.Getter;
import lombok.Setter;
import main.Library;
import user.entities.Artist;
import user.entities.Users;
import user.entities.audio.files.Episodes;
import user.entities.audio.files.Songs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void returnWrapped(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());

        if (user.getUserType() == Users.UserType.ARTIST) {
            userType = Users.UserType.ARTIST;
            Artist artist = (Artist) user;

            if (!artist.hasTrueValue()) {
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
        updateMapsForUsers(command, library, user);

        if (user.getTopSongs().isEmpty()) {
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

    private void sortList(List<Map.Entry<String, Integer>> entryList) {
        // Sort the list by values in descending order, and then by keys in lexicographic order
        entryList.sort(Map.Entry.<String, Integer>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()));

        // Get first 5 results
        entryList.subList(Math.min(entryList.size(), maxTop), entryList.size()).clear();
    }

    private void updateMapsForUsers(final Command command, final Library library, Users user) {
        Load load = new Load();
        if (user.getMusicPlayer() == null) {
            return;
        }
        Songs oldSong = user.getMusicPlayer().getSong();

        command.setUsername(user.getUsername());
        Status status = new Status();
        status.returnStatus(command, library);

        if (user.getTrackType() == Users.Track.ALBUM) {

            // if we haven't finish the album
            if (user.isSomethingLoaded()) {
                Songs currentSong = user.getMusicPlayer().getSong();
                ArrayList<Songs> songsToBeUpdated = new ArrayList<>(getSongsBetween(user.getMusicPlayer().getAlbum().getSongs(), oldSong, currentSong));
                for (Songs song : songsToBeUpdated) {
                    load.updateMaps(song, user, library);
                }
            } else {

                if (user.getMusicPlayer().getAlbum() == null) {
                    return;
                }

                Songs lastSong = user.getMusicPlayer().getAlbum().getSongs().get(user.getMusicPlayer().getAlbum().getSongs().size() - 1);
                ArrayList<Songs> songsToBeUpdated = new ArrayList<>(getSongsBetween(user.getMusicPlayer().getAlbum().getSongs(), oldSong, lastSong));
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
            load.updateTopEpisodes(currentEpisode, user);

            for (Episodes episode : user.getMusicPlayer().getPodcast().getEpisodes()) {
                // if we reached current episode break
                // else if it is the first episode, don't add
                if (episode.getName().equals(currentEpisode.getName())) {
                    break;
                } else if (episode != user.getMusicPlayer().getPodcast().getEpisodes().get(0)) {
                    // add it to the maps
                    load.updateTopEpisodes(episode, user);
                }
            }
        }
    }

    // Method to get songs between two given songs
    // Method to get songs between two given songs (exclusive)
    private static List<Songs> getSongsBetween(List<Songs> songList, Songs song1, Songs song2) {
        int startIndex = songList.indexOf(song1);
        int endIndex = songList.indexOf(song2);

        if (startIndex >= 0 && endIndex >= 0 && startIndex < endIndex) {
            // Create a new list and add all songs between startIndex + 1 and endIndex
            return new ArrayList<>(songList.subList(startIndex + 1, endIndex + 1));
        } else {
            // Handle the case where either song1 or song2 is not found in the list or startIndex is not less than endIndex
            return new ArrayList<>();
        }
    }
}
