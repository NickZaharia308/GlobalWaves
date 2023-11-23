package main;

import java.util.ArrayList;

public class SwitchVisibility extends Command {
    private String message;

    public void returnSwitchVisibility (Command command, Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        ArrayList<Users> users = library.getUsers();
        Users user = users.get(1);
        for (Users user1 : users) {
            if (user1.getUsername().equals(command.getUsername())) {
                user = user1;
                break;
            }
        }

        if (user.getNoOfPlaylists() < command.getPlaylistId()) {
            setMessage("The specified playlist ID is too high.");
            return;
        }

        ArrayList<Playlists> playlists = library.getPlaylists();
        Playlists playlist = null;
        for (Playlists playlistToFind: playlists) {
            // If the ID matches and the playlist's owner is the user itself
            if (playlistToFind.getOwner().equals(user.getUsername()) && playlistToFind.getUsersID() == command.getPlaylistId()) {
                playlist = playlistToFind;
                break;
            }
        }

        if (playlist.getVisibility().equals("public")) {
            setMessage("Visibility status updated successfully to private.");
            playlist.setVisibility("private");
        } else {
            setMessage("Visibility status updated successfully to public.");
            playlist.setVisibility("public");
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
