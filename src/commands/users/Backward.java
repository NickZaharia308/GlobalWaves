package commands.users;

import commands.Command;
import lombok.Getter;
import main.Library;
import userEntities.Users;
import userEntities.audio.Episodes;

/**
 * Represents a command to rewind with 90 seconds the current podcast episode.
 */
@Getter
public class Backward extends Command {

    private String message;
    private final int backwardTime = 90;

    /**
     * Sets the command, username, and timestamp based on the provided command.
     * Rewinds (by 90 seconds) the current podcast episode if conditions are met.
     *
     * @param command  The command containing user information.
     * @param library  The library containing user data and tracks.
     */
    public void returnBackward(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());

        // If there is nothing loaded
        if (!user.isSomethingLoaded()) {
            setMessage("Please load a source before rewinding.");
            return;
        }

        // If the source is not a podcast
        if (user.getTrackType() != Users.Track.PODCAST) {
            setMessage("The loaded source is not a podcast.");
            return;
        }

        Episodes currentEpisode = user.getMusicPlayer().getEpisode();
        // If less than 90 seconds have passed
        if (currentEpisode.getRemainingTime() + backwardTime > currentEpisode.getDuration()) {
            user.getMusicPlayer().setPlayTimestamp(command.getTimestamp());
            currentEpisode.setRemainingTime(currentEpisode.getDuration());
            setMessage("Rewound successfully.");
            return;
        }
        currentEpisode.setRemainingTime(currentEpisode.getRemainingTime() + backwardTime);
        setMessage("Rewound successfully.");
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
