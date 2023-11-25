package main;

import lombok.Getter;

import java.util.ArrayList;

/**
 * Represents a command to like or unlike the currently loaded song.
 */
@Getter
public class Like extends Command {

    private String message;

    /**
     * Sets the command, username, and timestamp based on the provided command.
     * Likes or unlikes the currently loaded song based on the user's action.
     * Every song has a HashMap with the users and likes (true or false)
     *
     * @param command  The command containing user information.
     * @param library  The library containing user data and tracks.
     */
    public void returnLike(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        // Finding the user
        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());

        // If there is nothing loaded
        if (!user.isSomethingLoaded()) {
            setMessage("Please load a source before liking or unliking.");
            return;
        }

        // If the loaded source is not a song
        if (user.getMusicPlayer().getSong() == null) {
            setMessage("Loaded source is not a song.");
            return;
        }
        Status status = new Status();
        status.returnStatus(command, library);

        // Finding the song in Songs collection
        ArrayList<Songs> songs = library.getSongs();
        Songs userSong = user.getMusicPlayer().getSong();
        ArrayList<Songs> likedSongs = user.getLikedSongs();
        for (Songs song : songs) {
            if (song.getName().equals(userSong.getName())) {
                for (Songs song2 : likedSongs) {
                    if (song2.getName().equals(userSong.getName())) {
                        // Remove the like from the HashMap
                        song.getUserLikesMap().put(user.getUsername(), false);
                        // Remove the song from the liked playlist
                        likedSongs.remove(song2);
                        setMessage("Unlike registered successfully.");
                        return;
                    }
                }
                // Add the player's like in the HashMap
                song.getUserLikesMap().put(user.getUsername(), true);

                // Add the song to the liked playlist
                likedSongs.add(song);

                setMessage("Like registered successfully.");
                break;
            }
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
