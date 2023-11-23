package main;

import java.util.ArrayList;

public class Forward extends Command {
    private String message;

    public void returnForward (Command command, Library library) {
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
            setMessage("Please load a source before attempting to forward.");
            return;
        }

        if (user.getTrackType() != Users.Track.PODCAST) {
            setMessage("The loaded source is not a podcast.");
            return;
        }

        Episodes currentEpisode = user.getMusicPlayer().getEpisode();

        // If there are less than 90 seconds remained in the episode
        if (currentEpisode.getRemainingTime() - 90 <= 0) {
            Next next = new Next();
            next.returnNext(command, library);
            setMessage("Skipped forward successfully.");
            return;
        }

        currentEpisode.setRemainingTime(currentEpisode.getRemainingTime() - 90);
        setMessage("Skipped forward successfully.");

    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
