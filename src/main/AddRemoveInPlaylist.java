package main;

import lombok.Getter;

import java.util.ArrayList;

/**
 * Represents an operation to add or remove a song in a playlist based on the provided command.
 */
@Getter
public class AddRemoveInPlaylist extends Command {
    private String message;

    /**
     * Performs the add or remove operation in a playlist based on the provided command.
     *
     * @param command The command containing user-specific information, timestamp, and playlist ID.
     * @param library The library containing songs, playlists, podcasts, and user information.
     */
    public void returnAddRemoveInPlaylist(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        final int playlistId = command.getPlaylistId();

        if (Playlists.getNoOfPlaylists() < playlistId) {
            setMessage("The specified playlist does not exist.");
            return;
        }

        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());

        //!!!!!
        if (user == null)
            return;

        // If there is nothing loaded
        if (!user.isSomethingLoaded()) {
            setMessage("Please load a source before adding to or removing from the playlist.");
            return;
        }

        // If the loaded source is not a song
        if (user.getTrackType() != Users.Track.SONG) {
            setMessage("The loaded source is not a song.");
            return;
        }

        final ArrayList<Playlists> playlists = library.getPlaylists();
        Playlists playlist = null;
        for (Playlists playlistToFind : playlists) {
            // If the ID matches and the playlist's owner is the user itself
            if (playlistToFind.getOwner().equals(user.getUsername()) && playlistToFind.getUsersID()
                                                == command.getPlaylistId()) {
                playlist = playlistToFind;
                break;
            }
        }

        if (playlist == null) {
            return;
        }

        final ArrayList<Songs> songs = playlist.getSongs();

        final Songs songToCheck = user.getMusicPlayer().getSong();
        if (!songs.contains(songToCheck)) {
            songs.add(songToCheck);
            setMessage("Successfully added to playlist.");
        } else {
            songs.remove(songToCheck);
            setMessage("Successfully removed from playlist.");
        }
    }

    /**
     * Sets the message generated during the add or remove operation in the playlist.
     *
     * @param message The operation message to be set.
     */
    public void setMessage(final String message) {
        this.message = message;
    }
}
