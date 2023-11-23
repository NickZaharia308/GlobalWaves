package main;

import java.util.ArrayList;

public class Next extends Command {
    private String message;
    public void returnNext (Command command, Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        ArrayList<Users> users = library.getUsers();
        Users user = users.get(1);
        for (Users user1 : users) {
            if (user1.getUsername().equals(command.getUsername())) {
                user = user1;
                break;
            }
        }

        // If there is nothing loaded
        if (!user.isSomethingLoaded()) {
            setMessage("Please load a source before skipping to the next track.");
            return;
        }

        user.getMusicPlayer().setRemainedTime(0);
        user.getMusicPlayer().setPlayTimestamp(command.getTimestamp());
        if (user.getTrackType() == Users.Track.PODCAST) {
            user.getMusicPlayer().getEpisode().setRemainingTime(0);
        }

        Status status = new Status();
        status.returnStatus(command, library);

        if (user.getMusicPlayer().getRemainedTime() == 0) {
            user.setSomethingLoaded(false);
        } else if (user.getTrackType() == Users.Track.SONG || user.getTrackType() == Users.Track.PLAYLIST) {
            setMessage("Skipped to next track successfully. The current track is " + user.getMusicPlayer().getSong().getName() + ".");
        } else if (user.getTrackType() == Users.Track.PODCAST) {
            setMessage("Skipped to next track successfully. The current track is " + user.getMusicPlayer().getEpisode().getName() + ".");
        }
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
