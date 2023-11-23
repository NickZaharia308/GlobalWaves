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

        ArrayList<Playlists> playlists = library.getPlaylists();
        Playlists playlist = null;
        for (Playlists playlistToFind: playlists) {
            // If the ID matches and the playlist's owner is the user itself
            if (playlistToFind.getOwner().equals(user.getUsername()) && playlistToFind.getUsersID() == command.getPlaylistId()) {
                playlist = playlistToFind;
                break;
            }
        }

        if (playlist == null)
            return;
        ArrayList<Songs> songs = playlist.getSongs();

        Songs songToCheck = user.getMusicPlayer().getSong();
        if (!songs.contains(songToCheck)) {
            songs.add(songToCheck);
            message = "Successfully added to playlist.";

        } else {
            songs.remove(songToCheck);
            message = "Successfully removed from playlist.";
        }

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
