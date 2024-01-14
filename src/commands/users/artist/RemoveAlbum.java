package commands.users.artist;

import commands.Command;
import commands.page.PageSubject;
import commands.users.Status;
import lombok.Getter;
import main.Library;
import user.entities.Artist;
import user.entities.Users;
import user.entities.audio.files.Album;
import user.entities.audio.files.Playlists;
import user.entities.audio.files.Songs;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * The {@code RemoveAlbum} class represents a command to remove an album of an artist.
 * It extends the {@code Command} class and is specific to artist-related operations.
 * The class notifies observers (Users) after a successful removal.
 */
@Getter
public class RemoveAlbum extends Command {
    private String message;

    /**
     * Processes the remove album command, removing the specified album for the artist user.
     *
     * @param command The command containing details about the album removal.
     * @param library The main library containing user data.
     */
    public void returnRemoveAlbum(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());
        super.setName(command.getName());

        Users user = new Users();
        user = user.getUser(library.getUsers(), this.getUsername());

        // Check if the user exists
        if (user == null) {
            setMessage("The username " + this.getUsername() + " doesn't exist.");
            return;
        }

        // Check if the user is an artist
        if (user.getUserType() != Users.UserType.ARTIST) {
            setMessage(this.getUsername() + " is not an artist.");
            return;
        }

        Artist artist = (Artist) user;
        Album albumToDelete = null;

        // Find the album to delete
        for (Album album : artist.getAlbums()) {
            if (album.getName().equals(this.getName())) {
                albumToDelete = album;
                break;
            }
        }

        // If there is no album with the given name
        if (albumToDelete == null) {
            setMessage(this.getUsername() + " doesn't have an album with the given name.");
            return;
        }

        // Check if the album is loaded for any user
        if (isAlbumLoaded(library, command, albumToDelete)) {
            setMessage(this.getUsername() + " can't delete this album.");
            return;
        }

        // Remove songs from everywhere and delete the album
        removeSongsFromEverywhere(library, albumToDelete);
        artist.getAlbums().remove(albumToDelete);

        // Notify the observers
        PageSubject pageSubject = new PageSubject();
        pageSubject.notifyObservers(artist.getUsername());

        setMessage(this.getUsername() + " deleted the album successfully.");
    }

    /**
     * Checks if the specified album is currently loaded by any user.
     *
     * @param library      The main library containing user data.
     * @param command      The command containing details about the album removal.
     * @param albumToDelete The album to be deleted.
     * @return True if the album is currently loaded; false otherwise.
     */
    private boolean isAlbumLoaded(final Library library, final Command command,
                                  final Album albumToDelete) {
        ArrayList<Users> allUsers = library.getUsers();

        for (Users user : allUsers) {
            Status status = new Status();
            command.setUsername(user.getUsername());
            status.returnStatus(command, library);

            // If the user has the album loaded and it is the album we want to delete
            if (user.isSomethingLoaded() && user.getTrackType() == Users.Track.ALBUM
                    && user.getMusicPlayer().getAlbum().getName()
                    .equals(albumToDelete.getName())) {
                return true;
            }

            // Iterate through songs of the album and check if one song is loaded
            if (user.isSomethingLoaded() && user.getTrackType() == Users.Track.SONG) {
                for (Songs song : albumToDelete.getSongs()) {
                    // If the name of the album song is the name of the song that user has on track
                    if (song.getName().equals(user.getMusicPlayer().getSong().getName())) {
                        return true;
                    }
                }
            }

            // Iterate through songs of the playlist and check if one song is also in the album
            if (user.isSomethingLoaded() && user.getTrackType() == Users.Track.PLAYLIST) {
                for (Songs songPlaylist : user.getMusicPlayer().getPlaylist().getSongs()) {
                    for (Songs song : albumToDelete.getSongs()) {
                        if (song.getName().equals(songPlaylist.getName())) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Removes songs from all playlists and liked songs that belong to the album being deleted.
     *
     * @param library      The main library containing user data.
     * @param albumToDelete The album to be deleted.
     */
    private void removeSongsFromEverywhere(final Library library, final Album albumToDelete) {
        // Delete all album songs from playlists
        ArrayList<Playlists> allPlaylists = library.getPlaylists();

        // Search in all playlists
        for (Playlists playlist : allPlaylists) {
            // Create an iterator for the playlist songs
            Iterator<Songs> playlistSongIterator = playlist.getSongs().iterator();

            // In all songs of the playlist
            while (playlistSongIterator.hasNext()) {
                Songs playlistSong = playlistSongIterator.next();

                // Compare to all songs of the album
                for (Songs songToDelete : albumToDelete.getSongs()) {
                    if (playlistSong.getName().equals(songToDelete.getName())) {
                        playlistSongIterator.remove();
                    }
                }
            }
        }

        // Remove songs from liked songs of all users
        ArrayList<Users> allUsers = library.getUsers();
        for (Users user : allUsers) {
            removeSongFromLikedSongs(user.getLikedSongs(), albumToDelete.getSongs());
        }
    }

    /**
     * Helper method to remove songs from the liked songs of a user.
     *
     * @param likedSongs    The liked songs of the user.
     * @param songsToRemove The songs to be removed from the liked songs.
     */
    private void removeSongFromLikedSongs(final ArrayList<Songs> likedSongs,
                                          final ArrayList<Songs> songsToRemove) {
        Iterator<Songs> likedSongsIterator = likedSongs.iterator();

        while (likedSongsIterator.hasNext()) {
            Songs likedSong = likedSongsIterator.next();
            for (Songs songToRemove : songsToRemove) {
                if (songToRemove.getName().equals(likedSong.getName())) {
                    likedSongsIterator.remove();
                }
            }
        }
    }

    /**
     * Sets the message for this RemoveAlbum instance.
     *
     * @param message The message to be set.
     */
    public void setMessage(final String message) {
        this.message = message;
    }
}
