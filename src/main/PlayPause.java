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
                Songs playerSong = user.getSelectedSong();
                if (playerSong.getPlayTimestamp() == -1) {
                    message = "Please load a source before attempting to pause or resume playback.";
                } else {
                    if (playerSong.isPaused()) {
                        message = "Playback resumed successfully.";
                        playerSong.setPaused(false);
                        playerSong.setPlayTimestamp(command.getTimestamp());

                    } else {
                        message = "Playback paused successfully.";
                        playerSong.setPaused(true);

                        // Computing the time elapsed until pause
                        int playTimestamp = playerSong.getPlayTimestamp();
                        int timestamp = command.getTimestamp();
                        int leftTime = playerSong.getRemainedTime() + playTimestamp - timestamp;

                        // Modifying the remaining time for a track accordingly
                        playerSong.setPlayTimestamp(timestamp);
                        playerSong.setRemainedTime(leftTime);
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
