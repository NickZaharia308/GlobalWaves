package main;

import java.util.ArrayList;

public class AddRemoveInPlaylist extends Command {
    private String message;
    public void returnAddRemoveInPlaylist (Command command, Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        int playlistId = command.getPlaylistId();

        if (Playlists.noOfPlaylists < playlistId) {
            message = "The specified playlist does not exist.";
            return;
        }

        // Finding the user
        ArrayList<Users> users = library.getUsers();
        Users user = null;
        for (Users user1 : users) {
            if (user1.getUsername().equals(command.getUsername())) {
                user = user1;
                break;
            }
        }

        // If there is nothing loaded
        if (!user.isSomethingLoaded()) {
            message = "Please load a source before adding to or removing from the playlist.";
            return;
        }

        // If the loaded source is not a song
        if (user.getTrackType() != Users.Track.SONG) {
            message = "The loaded source is not a song.";
            return;
        }

        Playlists playlist = library.getPlaylists().get(playlistId - 1);
        // If the playlist is public or the owner is the user itself
        if (playlist.getVisibilty().equals("public") || playlist.getOwner().equals(user.getUsername())) {
            ArrayList<Songs> songs = playlist.getSongs();

            Songs songToCheck = user.getSelectedSong();
            if (!songs.contains(songToCheck)) {
                songs.add(songToCheck);
                message = "Successfully added to playlist.";

            } else {
                songs.remove(songToCheck);
                message = "Successfully removed from playlist.";
            }
        }

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
