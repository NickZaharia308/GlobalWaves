package main;

import lombok.Getter;

/**
 * Represents a command to skip to the next track in the user's playlist.
 */
@Getter
public class Next extends Command {

    private String message;

    /**
     * Sets the command, username, and timestamp based on the provided command.
     * Updates the user's current MusicPlayer and skips to the next track.
     *
     * @param command  The command containing user information.
     * @param library  The library containing user data and tracks.
     */
    public void returnNext(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());

        // Update the user's current MusicPlayer
        Status status = new Status();
        status.returnStatus(command, library);

        // If there is nothing loaded
        if (!user.isSomethingLoaded()) {
            setMessage("Please load a source before skipping to the next track.");
            return;
        }

        // Set of instructions that set the remained time for a track to 0
        // (so it has to go to the next track)
        user.getMusicPlayer().setRemainedTime(0);
        user.getMusicPlayer().setPlayTimestamp(command.getTimestamp());
        if (user.getTrackType() == Users.Track.PODCAST) {
            user.getMusicPlayer().getEpisode().setRemainingTime(0);
        }

        // Go to the next track, by using status object
        status.returnStatus(command, library);

        if (user.getMusicPlayer().getRemainedTime() == 0) {
            user.setSomethingLoaded(false);
            setMessage("Please load a source before skipping to the next track.");
        } else if (user.getTrackType() == Users.Track.SONG || user.getTrackType()
                                                            == Users.Track.PLAYLIST) {
            setMessage("Skipped to next track successfully. The current track is "
                    + user.getMusicPlayer().getSong().getName() + ".");
        } else if (user.getTrackType() == Users.Track.PODCAST) {
            setMessage("Skipped to next track successfully. The current track is "
                    + user.getMusicPlayer().getEpisode().getName() + ".");
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
