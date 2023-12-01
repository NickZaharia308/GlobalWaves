package main;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Represents a command to retrieve the top 5 songs based on the number of likes.
 */
@Getter
public class GetTop5Songs extends Command {

    private String message;
    private final ArrayList<Songs> topSongs = new ArrayList<>();
    private final int maxSongs = 5;

    /**
     * Sets the command, username, and timestamp based on the provided command.
     * Retrieves the top 5 songs based on the number of likes in the library.
     *
     * @param command  The command containing user information.
     * @param library  The library containing user data and tracks.
     */
    public void returnGetTop5Songs(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        ArrayList<Songs> songsTopFive = new ArrayList<>(library.getSongs());

        // Sort songs based on the number of likes
        songsTopFive.sort(Comparator.comparingLong(Songs::getNumberOfLikes).reversed());

        // Get the top 5 songs
        int topCount = Math.min(maxSongs, songsTopFive.size());

        for (int i = 0; i < topCount; i++) {
            topSongs.add(songsTopFive.get(i));
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
