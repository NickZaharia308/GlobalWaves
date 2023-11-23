package main;

import java.util.ArrayList;
import java.util.LinkedList;

public class FollowPlaylist extends Command {
    private String message;

    public void returnFollowPlaylist (Command command, Library library) {
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


        // If there is nothing loaded
        if (!user.isSomethingSelected()) {
            setMessage("Please select a source before following or unfollowing.");
            return;
        }

        if (user.getTrackType() != Users.Track.PLAYLIST) {
            setMessage("The selected source is not a playlist.");
            return;
        }

        Playlists currentPlaylist = user.getMusicPlayer().getPlaylist();
        if (currentPlaylist.getOwner().equals(user.getUsername())) {
            setMessage("You cannot follow or unfollow your own playlist.");
            return;
        }

        // If the user followed the playlist
        if (currentPlaylist.getUserFollowMap().containsKey(user.getUsername()) &&
            currentPlaylist.getUserFollowMap().get(user.getUsername())) {

            currentPlaylist.setFollowers(currentPlaylist.getFollowers() - 1);
            currentPlaylist.getUserFollowMap().put(user.getUsername(), false);
            setMessage("Playlist unfollowed successfully.");
        } else {
            currentPlaylist.setFollowers(currentPlaylist.getFollowers() + 1);
            currentPlaylist.getUserFollowMap().put(user.getUsername(), true);
            setMessage("Playlist followed successfully.");
        }

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
