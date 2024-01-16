package commands.searchBar;

import commands.Command;
import commands.page.observer.PageSubject;
import lombok.Getter;
import main.Library;
import user.entities.audio.MusicPlayer;
import user.entities.Users;
import user.entities.audio.files.Album;
import user.entities.audio.files.Playlists;
import user.entities.audio.files.Podcasts;
import user.entities.audio.files.Songs;
import user.entities.specialEntities.PageMenu;

import java.util.ArrayList;
import java.util.LinkedList;


/**
 * Represents a selection operation based on the provided command and updates the selected item
 * and message.
 * The selection is performed from the search results, and the type of the selected item can be a:
 * song, playlist, podcast, album, or a page (Host page or Artist page).
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
     * a song, playlist, podcast, album, artist/host pages
     *
     * @param command The selection command containing the item number to be selected.
     * @param library The library containing songs, playlists, and podcasts and also users.
     */
    public void returnSelect(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        int itemNumber = command.getItemNumber();

        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());

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
                pageSearched(user, itemNumber);
                user.setSomethingSelected(false);

                // No track will play if we select a page
                user.setTrackType(null);
            }

            user.setSearchResults(null);
            user.setNoOfSearchResults(-1);
            user.setPageSearched(false);
        }
    }

    /**
     * Handles the selection of a song, playlist, podcast, or album based on the search results.
     *
     * @param user      The user performing the selection.
     * @param library   The library containing songs, playlists, podcasts, and albums.
     * @param itemNumber The number corresponding to the selected item in the search results.
     */
    private void trackSearched(final Users user, final Library library, final int itemNumber) {
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
            Album selectedAlbum = user.getAlbumResults().get(itemNumber - 1);

            // Finding the selected album and adding it to user
            ArrayList<Album> albums = library.getAlbums();
            for (Album album : albums) {
                if (album.equals(selectedAlbum)) {
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

    /**
     * Handles the selection of an artist or host page based on the search results.
     *
     * @param user      The user performing the selection.
     * @param itemNumber The number corresponding to the selected item in the search results.
     */
    private void pageSearched(final Users user, final int itemNumber) {

        LinkedList<String> searchResults = user.getSearchResults();
        String pageOwner = searchResults.get(itemNumber - 1);
        user.getPageMenu().setPageOwnerName(pageOwner);



        if (user.getPageMenu().getCurrentPage() == PageMenu.Page.ARTISTPAGE
                || user.getPageMenu().getCurrentPage() == PageMenu.Page.HOSTPAGE) {
            setMessage("Successfully selected " + pageOwner + "'s page.");

            // Adding the user as an "Observer" to artist or host (Subject)
            PageSubject pageSubject = new PageSubject();
            pageSubject.addObserver(pageOwner, user);
            pageSubject.notifyObservers(pageOwner);
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
