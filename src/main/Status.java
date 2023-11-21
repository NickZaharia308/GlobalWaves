package main;

import java.util.ArrayList;
import java.util.LinkedList;

public class Status extends Command {
    private String trackName;
    private int remainedTime;
    private boolean repeat, shuffle, paused;

    public void returnStatus (Command command, Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        ArrayList<Users> users = library.getUsers();
        for (Users user : users) {
            if (user.getUsername().equals(command.getUsername())) {

                if (user.isSomethingLoaded()) {
                    Songs playerSong = user.getSelectedSong();

                    // !!! To be modified
                    setTrackName(playerSong.getName());
                    if (playerSong.getPlayTimestamp() == -1) {
                        setRemainedTime(playerSong.getRemainedTime());
                    } else if (!playerSong.isPaused()) {
                        // Computing the remaining time for a track
                        int playTimestamp = playerSong.getPlayTimestamp();
                        int timestamp = command.getTimestamp();
                        int leftTime = playerSong.getRemainedTime() + playTimestamp - timestamp;

                        // If the remaining time > 0, set the value to it, else set the value to 0
                        if (leftTime > 0) {
                            playerSong.setPlayTimestamp(timestamp);
                            playerSong.setRemainedTime(leftTime);
                            setRemainedTime(leftTime);
                        } else {
                            setTrackName("");
                            playerSong.setPaused(true);

                            setRemainedTime(0);
                            playerSong.setRemainedTime(0);
                        }
                    } else if (playerSong.isPaused()) {
                        int leftTime = playerSong.getRemainedTime();
                        setRemainedTime(leftTime);
                    }
                    if (playerSong.isPaused())
                        setPaused(true);
                    else
                        setPaused(false);

                    setRepeat(false);
                    setShuffle(false);

                }
                break;
            }
        }

    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public int getRemainedTime() {
        return remainedTime;
    }

    public void setRemainedTime(int remainedTime) {
        this.remainedTime = remainedTime;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public boolean isShuffle() {
        return shuffle;
    }

    public void setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}
