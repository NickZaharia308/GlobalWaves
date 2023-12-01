package main;

import lombok.Getter;
@Getter
public class Repeat extends Command {
    private String message;

    /**
     * Updates the repeat mode based on the user's command.
     *
     * @param command  The command containing information about the repeat mode change.
     * @param library  The library containing playlists, songs, and user information.
     */
    public void returnRepeat(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());

        if (!user.isSomethingLoaded()) {
            setMessage("Please load a source before setting the repeat status.");
            return;
        }

        // Update the status before changing the repeat mode
        Status updateStatus = new Status();
        updateStatus.returnStatus(command, library);

        // Incrementing the status of RepeatMode variable (0->1->2->0)
        user.getMusicPlayer().setRepeatMode(user.getMusicPlayer().getRepeatMode() + 1);
        int repeatMode = user.getMusicPlayer().getRepeatMode();

        // If a playlist is loaded
        if (user.getTrackType() == Users.Track.PLAYLIST) {
            switch (repeatMode) {
                case 1:
                    setMessage("Repeat mode changed to repeat all.");
                    break;
                case 2:
                    setMessage("Repeat mode changed to repeat current song.");
                    break;
                default:
                    setMessage("Repeat mode changed to no repeat.");
                    break;
            }
        } else {
            switch (repeatMode) {
                case 1:
                    setMessage("Repeat mode changed to repeat once.");
                    break;
                case 2:
                    setMessage("Repeat mode changed to repeat infinite.");
                    break;
                default:
                    setMessage("Repeat mode changed to no repeat.");
                    break;
            }
        }
    }

    /**
     * Sets the message for the repeat operation.
     *
     * @param message  The message to be set.
     */
    public void setMessage(final String message) {
        this.message = message;
    }
}
