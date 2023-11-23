package main;

import java.util.ArrayList;

public class Repeat extends Command {
    private String message;

    public void returnRepeat (Command command, Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        ArrayList<Users> users = library.getUsers();
        for (Users user : users) {
            if (user.getUsername().equals(command.getUsername())) {
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
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
