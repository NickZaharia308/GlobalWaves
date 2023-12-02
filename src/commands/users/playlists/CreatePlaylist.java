package commands.users.playlists;

import commands.Command;
import lombok.Getter;
import main.Library;
import userEntities.audio.Playlists;

import java.util.ArrayList;

@Getter
public class CreatePlaylist extends Command {
    private String message;

    /**
     * Creates a new playlist based on the provided command and adds it to the library.
     * It also creates the playlist by keeping in mind how many playlist has the give user
     *
     * @param command  The command containing information about the playlist creation.
     * @param library  The library containing playlists, songs, and user information.
     */
    public void returnCreatePlaylist(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        final String playlistName = command.getPlaylistName();
        final String username = command.getUsername();

        // Searching if the given playlist already exists
        final ArrayList<Playlists> playlists = library.getPlaylists();
        for (final Playlists playlist : playlists) {
            if (playlist.getName().equals(playlistName) && playlist.getOwner().equals(username)) {
                setMessage("A playlist with the same name already exists.");
                return;
            }
        }

        // Creating the new playlist
        final Playlists playlist = new Playlists(playlistName, username, library);
        playlists.add(playlist);
        setMessage("Playlist created successfully.");
    }

    /**
     * Sets the message for the playlist creation operation.
     *
     * @param message  The message to be set.
     */
    public void setMessage(final String message) {
        this.message = message;
    }
}
