package main;

import java.util.ArrayList;

public class Shuffle extends Command {
    private String message;

    public void returnShuffle (Command command, Library library) {
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

        Status status = new Status();
        status.returnStatus(command, library);

        // If there is nothing loaded
        if (!user.isSomethingLoaded()) {
            setMessage("Please load a source before using the shuffle function.");
            return;
        }

        // If the loaded source is not a song
        if (!(user.getTrackType() == Users.Track.PLAYLIST)) {
            setMessage("The loaded source is not a playlist.");
            return;
        }

        int seed = command.getSeed();
        if (!user.getMusicPlayer().isShuffled()) {
            user.getMusicPlayer().setShuffled(true);

            Playlists originalPlaylist = user.getMusicPlayer().getPlaylist();
            Playlists shuffledPlaylist = new Playlists(originalPlaylist, seed);
            user.getMusicPlayer().setPlaylistsShuffled(shuffledPlaylist);

            // Swap values
            Playlists temp = originalPlaylist;
            originalPlaylist = shuffledPlaylist;
            shuffledPlaylist = temp;

            user.getMusicPlayer().setPlaylist(originalPlaylist);
            user.getMusicPlayer().setPlaylistsShuffled(shuffledPlaylist);

            setMessage("Shuffle function activated successfully.");
        } else {
            user.getMusicPlayer().setShuffled(false);


            // Set the value for playlist to the original playlist
            user.getMusicPlayer().setPlaylist(user.getMusicPlayer().getPlaylistsShuffled());
            setMessage("Shuffle function deactivated successfully.");
        }

    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
