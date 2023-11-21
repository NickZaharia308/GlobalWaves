package main;

import java.util.ArrayList;
import java.util.LinkedList;

public class Load extends Command {
    private String message;
    public void returnLoad (Command command, Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        ArrayList<Users> users = library.getUsers();
        for (Users user : users) {
            if (user.getUsername().equals(command.getUsername())) {
                if (!user.isSomethingSelected()) {
                    message = "Please select a source before attempting to load.";
                } else {
                    message = "Playback loaded successfully.";
                    user.setSomethingLoaded(true);

                    // Setup for when a song is played by a user
                    user.setTrackType(Users.Track.SONG);
                    Songs playerSong = user.getSelectedSong();
                    playerSong.setPlayTimestamp(command.getTimestamp());
                    playerSong.setPaused(false);
                    playerSong.setRemainedTime(playerSong.getDuration());
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
