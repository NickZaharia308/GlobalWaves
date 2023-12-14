package commands.statistics;

import lombok.Getter;
import commands.Command;
import main.Library;
import user.entities.audio.files.Playlists;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Represents a command to retrieve the top 5 playlists based on the number of followers.
 */
@Getter
public class GetTop5Playlists extends Command {
    private String message;
    private ArrayList<Playlists> playlists = new ArrayList<>();
    private final int maxPlaylists = 5;

    /**
     * Retrieves the top 5 playlists based on the number of followers.
     *
     * @param command The command containing user-specific information, timestamp,
     *                and request details.
     * @param library The library containing playlists and user information.
     */
    public void returnGetTop5Playlists(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        ArrayList<Playlists> playlistsTopFive = new ArrayList<>(library.getPlaylists());

        // Sort playlists based on followers count
        playlistsTopFive.sort(Comparator.comparingInt(Playlists::getFollowers).reversed());

        // Get the top 5 playlists or less
        int topCount = Math.min(maxPlaylists, playlistsTopFive.size());

        playlists.clear(); // Clear existing playlists

        for (int i = 0; i < topCount; i++) {
            playlists.add(playlistsTopFive.get(i));
        }
    }

    /**
     * Sets the message associated with the execution of the command.
     *
     * @param message The message to be set.
     */
    public void setMessage(final String message) {
        this.message = message;
    }
}
