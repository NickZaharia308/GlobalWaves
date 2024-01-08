package commands.users.playlists;

import commands.Command;
import lombok.Getter;
import main.Library;
import user.entities.Users;
import user.entities.audio.files.Playlists;

import java.util.ArrayList;

/**
 * Represents a command to switch the visibility status of a playlist.
 */
@Getter
public class SwitchVisibility extends Command {
    private String message;

    /**
     * Executes the command to switch the visibility status of a playlist.
     *
     * @param command The command containing information about the operation.
     * @param library The library containing playlists and user information.
     */
    public void returnSwitchVisibility(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());

        if (user.getNoOfPlaylists() < command.getPlaylistId()) {
            setMessage("The specified playlist ID is too high.");
            return;
        }

        ArrayList<Playlists> playlists = library.getPlaylists();
        Playlists playlist = null;

        for (Playlists playlistToFind : playlists) {
            // If the ID matches and the playlist's owner is the user itself
            if (playlistToFind.getOwner().equals(user.getUsername()) && playlistToFind.getUsersID()
                                                                    == command.getPlaylistId()) {
                playlist = playlistToFind;
                break;
            }
        }

        if ("public".equals(playlist.getVisibility())) {
            setMessage("Visibility status updated successfully to private.");
            playlist.setVisibility("private");
        } else {
            setMessage("Visibility status updated successfully to public.");
            playlist.setVisibility("public");
        }
    }

    /**
     * Sets the message associated with the command.
     *
     * @param message The message to be set.
     */
    public void setMessage(final String message) {
        this.message = message;
    }
}
