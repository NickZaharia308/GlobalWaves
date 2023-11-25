package main;

import lombok.Getter;

/**
 * Represents a command to follow or unfollow a playlist.
 */
@Getter
public class FollowPlaylist extends Command {

    private String message;

    /**
     * Sets the command, username, and timestamp based on the provided command.
     * Follows or unfollows the selected playlist based on user action.
     * The user can follow the playlist only if its visibility is "public"
     *
     * @param command  The command containing user information.
     * @param library  The library containing user data and tracks.
     */
    public void returnFollowPlaylist(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());

        // If there is nothing selected
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
        if (currentPlaylist.getUserFollowMap().containsKey(user.getUsername())
                && currentPlaylist.getUserFollowMap().get(user.getUsername())) {

            currentPlaylist.setFollowers(currentPlaylist.getFollowers() - 1);
            currentPlaylist.getUserFollowMap().put(user.getUsername(), false);
            setMessage("Playlist unfollowed successfully.");
        } else {
            currentPlaylist.setFollowers(currentPlaylist.getFollowers() + 1);
            currentPlaylist.getUserFollowMap().put(user.getUsername(), true);
            setMessage("Playlist followed successfully.");
        }
    }

    /**
     * Sets the message for the command.
     *
     * @param message The message to be set.
     */
    public void setMessage(final String message) {
        this.message = message;
    }
}
