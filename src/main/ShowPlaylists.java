package main;

import lombok.Getter;

import java.util.ArrayList;

/**
 * Represents a command to retrieve and display user-specific playlists (playlists created by user)
 */
@Getter
public class ShowPlaylists extends Command {

    private ArrayList<Playlists> retrievedPlaylists;

    /**
     * Sets the command, username, and timestamp based on the provided command.
     * Retrieves the user's created playlists (Sets the local parameter).
     *
     * @param command  The command containing user information.
     * @param library  The library containing user data and playlists.
     */
    public void returnShowPlaylists(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        ArrayList<Playlists> playlists = library.getPlaylists();
        ArrayList<Playlists> userPlaylist = new ArrayList<>();

        for (Playlists playlist : playlists) {
            if (playlist.getOwner().equals(command.getUsername())) {
                userPlaylist.add(playlist);
            }
        }
        setRetrievedPlaylists(userPlaylist);
    }

    /**
     * Sets the playlists for the command.
     *
     * @param retrievedPlaylists The playlists to be set.
     */
    public void setRetrievedPlaylists(final ArrayList<Playlists> retrievedPlaylists) {
        this.retrievedPlaylists = retrievedPlaylists;
    }
}
