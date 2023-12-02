package commands.users;

import commands.Command;
import lombok.Getter;
import main.Library;
import userEntities.Users;

@Getter
public class PlayPause extends Command {
    private String message;

    /**
     * Resumes or pauses playback based on the provided command and updates the user's status.
     *
     * @param command  The command containing information about the play/pause operation.
     * @param library  The library containing playlists, songs, and user information.
     */
    public void returnPlayPause(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        final Status status = new Status();
        status.returnStatus(command, library);

        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());

        if (!user.isSomethingLoaded()) {
            setMessage("Please load a source before attempting to pause or resume playback.");
        } else {
            if (user.getMusicPlayer().isPaused()) {
                setMessage("Playback resumed successfully.");
                user.getMusicPlayer().setPaused(false);
                user.getMusicPlayer().setPlayTimestamp(command.getTimestamp());

            } else {
                setMessage("Playback paused successfully.");
                user.getMusicPlayer().setPaused(true);

                // Computing the time elapsed until pause
                final int playTimestamp = user.getMusicPlayer().getPlayTimestamp();
                final int timestamp = command.getTimestamp();
                final int leftTime = user.getMusicPlayer().getRemainedTime()
                                    + playTimestamp - timestamp;

                // Modifying the remaining time for a track accordingly
                user.getMusicPlayer().setPlayTimestamp(timestamp);
                user.getMusicPlayer().setRemainedTime(leftTime);
            }
        }
    }

    /**
     * Sets the message for the play/pause operation.
     *
     * @param message  The message to be set.
     */
    public void setMessage(final String message) {
        this.message = message;
    }
}
