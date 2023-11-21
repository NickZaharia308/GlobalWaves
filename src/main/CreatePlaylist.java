package main;

import java.util.ArrayList;

public class CreatePlaylist extends Command {
    private String message;
    public void returnCreatePlaylist (Command command, Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        String playlistName = command.getPlaylistName();
        String username = command.getUsername();

        // Searching if the given playlist already exists
        ArrayList<Playlists> playlists = library.getPlaylists();
        for (Playlists playlist : playlists) {
            if (playlist.getName().equals(playlistName) && playlist.getOwner().equals(username)) {
                message = "A playlist with the same name already exists.";
                return;
            }
        }

        // Creating the new playlist
        Playlists playlist = new Playlists(playlistName, username);
        playlists.add(playlist);
        message = "Playlist created successfully.";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
