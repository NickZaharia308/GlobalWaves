package main;

import java.util.ArrayList;

public class Backward extends Command {
    private String message;

    public void returnBackward (Command command, Library library) {
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
            setMessage("Please load a source before rewinding.");
            return;
        }

        if (user.getTrackType() != Users.Track.PODCAST) {
            setMessage("The loaded source is not a podcast.");
            return;
        }

        Episodes currentEpisode = user.getMusicPlayer().getEpisode();
        // If less than 90 seconds have passed
        if (currentEpisode.getRemainingTime() + 90 > currentEpisode.getDuration()) {
            user.getMusicPlayer().setPlayTimestamp(command.getTimestamp());
            currentEpisode.setRemainingTime(currentEpisode.getDuration());
            setMessage("Rewound successfully.");
            return;
        }
        currentEpisode.setRemainingTime(currentEpisode.getRemainingTime() + 90);
        setMessage("Rewound successfully.");
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
