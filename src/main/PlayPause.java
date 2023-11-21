package main;

import java.util.ArrayList;
import java.util.LinkedList;

public class PlayPause extends Command {
    private String message;

    public void returnPlayPause (Command command, Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        ArrayList<Users> users = library.getUsers();
        for (Users user : users) {
            if (user.getUsername().equals(command.getUsername())) {
                if (user.getMusicPlayer().getPlayTimestamp() == -1) {
                    message = "Please load a source before attempting to pause or resume playback.";
                } else {
                    if (user.getMusicPlayer().isPaused()) {
                        message = "Playback resumed successfully.";
                        user.getMusicPlayer().setPaused(false);
                        user.getMusicPlayer().setPlayTimestamp(command.getTimestamp());

                    } else {
                        message = "Playback paused successfully.";
                        user.getMusicPlayer().setPaused(true);

                        // Computing the time elapsed until pause
                        int playTimestamp = user.getMusicPlayer().getPlayTimestamp();
                        int timestamp = command.getTimestamp();
                        int leftTime = user.getMusicPlayer().getRemainedTime() + playTimestamp - timestamp;

                        // Modifying the remaining time for a track accordingly
                        user.getMusicPlayer().setPlayTimestamp(timestamp);
                        user.getMusicPlayer().setRemainedTime(leftTime);
                    }
                }
                break;
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
