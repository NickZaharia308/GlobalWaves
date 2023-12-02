package commands.users;

import commands.Command;
import lombok.Getter;
import main.Library;
import userEntities.Users;
import userEntities.audio.Episodes;

/**
 * Represents a command to forward with 90 seconds the current podcast episode.
 */
@Getter
public class Forward extends Command {

    private String message;
    private final int forwardTime = 90;

    /**
     * Sets the command, username, and timestamp based on the provided command.
     * Forwards (by 90 seconds) the current podcast episode if conditions are met.
     *
     * @param command  The command containing user information.
     * @param library  The library containing user data and tracks.
     */
    public void returnForward(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());

        // If there is nothing loaded
        if (!user.isSomethingLoaded()) {
            setMessage("Please load a source before attempting to forward.");
            return;
        }

        if (user.getTrackType() != Users.Track.PODCAST) {
            setMessage("The loaded source is not a podcast.");
            return;
        }

        Episodes currentEpisode = user.getMusicPlayer().getEpisode();

        // If there are less than 90 seconds remained in the episode
        if (currentEpisode.getRemainingTime() - forwardTime <= 0) {
            Next next = new Next();
            next.returnNext(command, library);
            setMessage("Skipped forward successfully.");
            return;
        }

        currentEpisode.setRemainingTime(currentEpisode.getRemainingTime() - forwardTime);
        setMessage("Skipped forward successfully.");
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
