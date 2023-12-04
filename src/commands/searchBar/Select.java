package commands.searchBar;

import commands.Command;
import lombok.Getter;
import main.Library;
import userEntities.MusicPlayer;
import userEntities.Users;
import userEntities.audio.Album;
import userEntities.audio.Playlists;
import userEntities.audio.Podcasts;
import userEntities.audio.Songs;
import userEntities.specialEntities.PageMenu;

import java.util.ArrayList;
import java.util.LinkedList;


/**
 * Represents a selection operation based on the provided command and updates the selected item
 * and message.
 * The selection is performed from the search results, and the type of the selected item can be a:
 * song, playlist, or podcast.
 */
@Getter
public class Select extends Command {
    /**
     * -- GETTER --
     *  Gets the message generated during the selection operation.
     *
     * @return The selection message.
     */
    private String message;

    /**
     * Performs a selection based on the provided command and updates the selected item and message.
     * The selection is performed from the search results, and the type of the selected item can be
     * a song, playlist, or podcast.
     *
     * @param command The selection command containing the item number to be selected.
     * @param library The library containing songs, playlists, and podcasts.
     */
    public void returnSelect(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        int itemNumber = command.getItemNumber();

        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());

        //!!!!!
        if (user == null)
            return;

        if (user.getNoOfSearchResults() == -1) {
            user.setSomethingSelected(false);
            setMessage("Please conduct a search before making a selection.");
            return;
        } else if (itemNumber > user.getNoOfSearchResults()) {
            user.setSomethingSelected(false);
            setMessage("The selected ID is too high.");
            return;
        } else {

            // If a song, playlist, podcast was searched
            if (!user.isPageSearched()) {
                trackSearched(user, library, itemNumber);
                user.setSomethingSelected(true);
            } else {
                pageSearched(user, library, itemNumber);
                user.setSomethingSelected(false);

                // No track will play if we select a page
                user.setTrackType(null);
            }

            user.setSearchResults(null);
            user.setNoOfSearchResults(-1);
            user.setPageSearched(false);
        }
    }

    private void trackSearched(Users user, Library library, int itemNumber) {
        LinkedList<String> searchResults = user.getSearchResults();

        // Creating the musicPlayer if it doesn't exist
        MusicPlayer musicPlayer = user.getMusicPlayer();
        if (user.getMusicPlayer() == null) {
            musicPlayer = new MusicPlayer();
        }

        // If the track is a song
        if (user.getTrackType() == Users.Track.SONG) {
            String selectedSong = searchResults.get(itemNumber - 1);

            // Finding the selected song and adding it to user
            ArrayList<Songs> songs = library.getSongs();
            for (Songs song : songs) {
                if (song.getName().equals(selectedSong)) {
                    musicPlayer.setSong(song);
                    user.setMusicPlayer(musicPlayer);
                    break;
                }
            }

            setMessage("Successfully selected " + user.getMusicPlayer().getSong().getName()
                    + ".");
        } else if (user.getTrackType() == Users.Track.PLAYLIST) {
            String selectedPlaylist = searchResults.get(itemNumber - 1);

            // Finding the selected playlist and adding it to user
            ArrayList<Playlists> playlists = library.getPlaylists();
            for (Playlists playlist : playlists) {
                if (playlist.getName().equals(selectedPlaylist)) {
                    musicPlayer.setPlaylist(playlist);
                    user.setMusicPlayer(musicPlayer);
                    break;
                }
            }
            if (user.getMusicPlayer().getPlaylist() == null) {
                return;
            }
            setMessage("Successfully selected " + user.getMusicPlayer().getPlaylist().getName()
                    + ".");
        } else if (user.getTrackType() == Users.Track.PODCAST) {
            String selectedPodcast = searchResults.get(itemNumber - 1);

            // Finding the selected podcast and adding it to user
            if (musicPlayer.getPodcasts() == null) {
                musicPlayer.setPodcasts(library.getPodcasts());
            }
            for (Podcasts podcast : musicPlayer.getPodcasts()) {
                if (podcast.getName().equals(selectedPodcast)) {
                    musicPlayer.setPodcast(podcast);
                    user.setMusicPlayer(musicPlayer);
                    break;
                }
            }

            setMessage("Successfully selected " + user.getMusicPlayer().getPodcast().getName()
                    + ".");
        } else if (user.getTrackType() == Users.Track.ALBUM) {
            String selectedAlbum = searchResults.get(itemNumber - 1);

            // Finding the selected album and adding it to user
            ArrayList<Album> albums = library.getAlbums();
            for (Album album : albums) {
                if (album.getName().equals(selectedAlbum)) {
                    musicPlayer.setAlbum(album);
                    user.setMusicPlayer(musicPlayer);
                    break;
                }
            }
            if (user.getMusicPlayer().getAlbum() == null) {
                return;
            }
            setMessage("Successfully selected " + user.getMusicPlayer().getAlbum().getName()
                    + ".");
        }
    }

    private void pageSearched(Users user, Library library, int itemNumber) {

        LinkedList<String> searchResults = user.getSearchResults();

        if (user.getPageMenu().getCurrentPage() == PageMenu.Page.ARTISTPAGE) {
            String selectedArtist = searchResults.get(itemNumber - 1);
            user.getPageMenu().setArtistPage(user, library, selectedArtist);
            setMessage("Successfully selected " + selectedArtist +"'s page.");
        } else if (user.getPageMenu().getCurrentPage() == PageMenu.Page.HOSTPAGE) {
            String selectedHost = searchResults.get(itemNumber - 1);
            user.getPageMenu().setHostPage(user, library, selectedHost);
            setMessage("Successfully selected " + selectedHost +"'s page.");
        }
    }

    /**
     * Sets the selection message.
     *
     * @param message The selection message to be set.
     */
    public void setMessage(final String message) {
        this.message = message;
    }
}
