package main;

import java.util.ArrayList;
import java.util.LinkedList;

public class Status extends Command {
    private String trackName;
    private int remainedTime;
    private boolean shuffle, paused;
    private int repeat;
    private String repeatMessage;

    public void returnStatus (Command command, Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        ArrayList<Users> users = library.getUsers();
        for (Users user : users) {
            if (user.getUsername().equals(command.getUsername())) {

                if (user.isSomethingLoaded()) {
                    if (user.getTrackType() == Users.Track.SONG) {
                        Songs playerSong = user.getMusicPlayer().getSong();

                        // !!! To be modified
                        if (user.getMusicPlayer().getSong() == null)
                            return;
                        setTrackName(playerSong.getName());
                        if (user.getMusicPlayer().getPlayTimestamp() == -1) {
                            setRemainedTime(user.getMusicPlayer().getRemainedTime());
                        } else if (!user.getMusicPlayer().isPaused()) {
                            // Computing the remaining time for a track
                            int playTimestamp = user.getMusicPlayer().getPlayTimestamp();
                            int timestamp = command.getTimestamp();
                            int leftTime = user.getMusicPlayer().getRemainedTime() + playTimestamp - timestamp;

                            // If the remaining time > 0, set the value to it, else set the value to 0
                            if (leftTime > 0) {
                                user.getMusicPlayer().setPlayTimestamp(timestamp);
                                user.getMusicPlayer().setRemainedTime(leftTime);
                                setRemainedTime(leftTime);
                            } else {
                                // Repeat once
                                if (user.getMusicPlayer().getRepeatMode() == 1) {
                                    leftTime += playerSong.getDuration();
                                    if (leftTime > 0) {
                                        setTrackName(playerSong.getName());
                                        user.getMusicPlayer().setSong(playerSong);
                                        user.getMusicPlayer().setPlayTimestamp(timestamp);
                                        user.getMusicPlayer().setRemainedTime(leftTime);
                                        setRemainedTime(leftTime);

                                    } else {
                                        setTrackName("");
                                        user.getMusicPlayer().setPaused(true);

                                        setRemainedTime(0);
                                        user.getMusicPlayer().setRemainedTime(0);
                                    }
                                    // Reset the repeat mode
                                    user.getMusicPlayer().setRepeatMode(0);

                                    // Repeat infinite times
                                } else if (user.getMusicPlayer().getRepeatMode() == 2) {
                                    while (leftTime < 0) {
                                        leftTime += playerSong.getDuration();
                                    }
                                    setTrackName(playerSong.getName());
                                    user.getMusicPlayer().setSong(playerSong);
                                    user.getMusicPlayer().setPlayTimestamp(timestamp);
                                    user.getMusicPlayer().setRemainedTime(leftTime);
                                    setRemainedTime(leftTime);
                                } else {
                                    setTrackName("");
                                    user.getMusicPlayer().setPaused(true);

                                    setRemainedTime(0);
                                    user.getMusicPlayer().setRemainedTime(0);
                                    user.setSomethingLoaded(false);
                                }
                            }
                        } else if (user.getMusicPlayer().isPaused()) {
                            int leftTime = user.getMusicPlayer().getRemainedTime();
                            setRemainedTime(leftTime);
                        }

                    } else if (user.getTrackType() == Users.Track.PLAYLIST) {
                        Songs playerSong = user.getMusicPlayer().getSong();

                        // !!! To be modified
                        if (user.getMusicPlayer().getSong() == null)
                            return;
                        setTrackName(playerSong.getName());
                        if (user.getMusicPlayer().getPlayTimestamp() == -1) {
                            setRemainedTime(user.getMusicPlayer().getRemainedTime());
                        } else if (!user.getMusicPlayer().isPaused()) {
                            // Computing the remaining time for a track
                            int playTimestamp = user.getMusicPlayer().getPlayTimestamp();
                            int timestamp = command.getTimestamp();
                            int leftTime = user.getMusicPlayer().getRemainedTime() + playTimestamp - timestamp;

                            // If the remaining time > 0, set the value to it, else set the value to 0
                            if (leftTime > 0) {
                                user.getMusicPlayer().setPlayTimestamp(timestamp);
                                user.getMusicPlayer().setRemainedTime(leftTime);
                                setRemainedTime(leftTime);
                            } else {
                                // If the repeat mode is "repeat current song"
                                if (user.getMusicPlayer().getRepeatMode() == 2) {
                                    while (leftTime <= 0) {
                                        leftTime += playerSong.getDuration();
                                    }
                                    setTrackName(playerSong.getName());
                                    user.getMusicPlayer().setSong(playerSong);
                                    user.getMusicPlayer().setPlayTimestamp(timestamp);
                                    user.getMusicPlayer().setRemainedTime(leftTime);
                                    setRemainedTime(leftTime);

                                    repeatMessage(user.getMusicPlayer().getRepeatMode(), user.getTrackType());
                                    continue;
                                }

                                int index = user.getMusicPlayer().getPlaylist().getSongs().indexOf(playerSong);
                                Songs currentSong = playerSong;
                                while(index < user.getMusicPlayer().getPlaylist().getSongs().size() - 1 && leftTime <= 0) {
                                    index++;
                                    currentSong = user.getMusicPlayer().getPlaylist().getSongs().get(index);
                                    leftTime += currentSong.getDuration();

                                    // if the repeat mode is "repeat all and we reached the last song
                                    if (user.getMusicPlayer().getRepeatMode() == 1 && leftTime <= 0
                                        && index == user.getMusicPlayer().getPlaylist().getSongs().size() - 1) {
                                        index = -1;
                                    }
                                }
                                // if the repeat mode is "repeat all and we reached the last song
                                if (user.getMusicPlayer().getRepeatMode() == 1 && leftTime <= 0
                                        && index == user.getMusicPlayer().getPlaylist().getSongs().size() - 1) {
                                    currentSong = user.getMusicPlayer().getPlaylist().getSongs().get(0);
                                    leftTime += currentSong.getDuration();
                                }
                                if (leftTime > 0) {
                                    setTrackName(currentSong.getName());
                                    user.getMusicPlayer().setSong(currentSong);
                                    user.getMusicPlayer().setPlayTimestamp(timestamp);
                                    user.getMusicPlayer().setRemainedTime(leftTime);
                                    setRemainedTime(leftTime);
                                } else {
                                    setTrackName("");
                                    user.getMusicPlayer().setPaused(true);

                                    setRemainedTime(0);
                                    user.getMusicPlayer().setRemainedTime(0);
                                    user.setSomethingLoaded(false);
                                }
                            }
                        } else if (user.getMusicPlayer().isPaused()) {
                            int timestamp = command.getTimestamp();
                            int leftTime = user.getMusicPlayer().getRemainedTime();

                            if (leftTime > 0) {
                                user.getMusicPlayer().setPlayTimestamp(timestamp);
                                user.getMusicPlayer().setRemainedTime(leftTime);
                                setRemainedTime(leftTime);
                            } else {
                                // If the repeat mode is "repeat current song"
                                if (user.getMusicPlayer().getRepeatMode() == 2) {
                                    while (leftTime < 0) {
                                        leftTime += playerSong.getDuration();
                                    }
                                    setTrackName(playerSong.getName());
                                    user.getMusicPlayer().setSong(playerSong);
                                    user.getMusicPlayer().setPlayTimestamp(timestamp);
                                    user.getMusicPlayer().setRemainedTime(leftTime);
                                    setRemainedTime(leftTime);
                                    setPaused(false);

                                    repeatMessage(user.getMusicPlayer().getRepeatMode(), user.getTrackType());
                                    continue;
                                }

                                int index = user.getMusicPlayer().getPlaylist().getSongs().indexOf(playerSong);
                                Songs currentSong = playerSong;
                                while(index < user.getMusicPlayer().getPlaylist().getSongs().size() - 1 && leftTime <= 0) {
                                    index++;
                                    currentSong = user.getMusicPlayer().getPlaylist().getSongs().get(index);
                                    leftTime += currentSong.getDuration();

                                    // if the repeat mode is "repeat all and we reached the last song
                                    if (user.getMusicPlayer().getRepeatMode() == 1 && leftTime < 0
                                            && index == user.getMusicPlayer().getPlaylist().getSongs().size() - 1) {
                                        index = -1;
                                    }
                                }
                                if (leftTime > 0) {
                                    setTrackName(currentSong.getName());
                                    user.getMusicPlayer().setSong(currentSong);
                                    user.getMusicPlayer().setPaused(false);
                                    user.getMusicPlayer().setPlayTimestamp(timestamp);
                                    user.getMusicPlayer().setRemainedTime(leftTime);
                                    setRemainedTime(leftTime);
                                } else {
                                    setTrackName("");
                                    user.getMusicPlayer().setPaused(true);

                                    setRemainedTime(0);
                                    user.getMusicPlayer().setRemainedTime(0);
                                    user.setSomethingLoaded(false);
                                }
                            }
                        }
                    } else {
                        // Podcast case
                        Episodes playerEpisode = user.getMusicPlayer().getEpisode();

                        // !!! To be modified
                        if (user.getMusicPlayer().getEpisode() == null)
                            return;
                        setTrackName(playerEpisode.getName());
                        if (user.getMusicPlayer().getPlayTimestamp() == -1) {
                            setRemainedTime(user.getMusicPlayer().getRemainedTime());
                        } else if (!user.getMusicPlayer().isPaused()) {
                            // Computing the remaining time for a track
                            int playTimestamp = user.getMusicPlayer().getPlayTimestamp();
                            int timestamp = command.getTimestamp();
                            int leftTime = user.getMusicPlayer().getEpisode().getRemainingTime() + playTimestamp - timestamp;

                            // If the remaining time > 0, set the value to it, else set the value to 0
                            if (leftTime > 0) {
                                user.getMusicPlayer().setPlayTimestamp(timestamp);
                                user.getMusicPlayer().setRemainedTime(leftTime);
                                user.getMusicPlayer().getEpisode().setRemainingTime(leftTime);
                                setRemainedTime(leftTime);
                            } else {
                                // Get the whole podcast and find the next episode where "leftTIme > 0"
                                int index = user.getMusicPlayer().getPodcast().getEpisodes().indexOf(playerEpisode);
                                Episodes currentEpisode = playerEpisode;
                                while(index < user.getMusicPlayer().getPodcast().getEpisodes().size() - 1 && leftTime <= 0) {
                                    index++;
                                    currentEpisode = user.getMusicPlayer().getPodcast().getEpisodes().get(index);
                                    leftTime += currentEpisode.getRemainingTime();
                                    // If all the episodes finished, reset the episodes
                                    if (index == user.getMusicPlayer().getPodcast().getEpisodes().size() - 1) {
                                        user.getMusicPlayer().getPodcast().resetEpisodes(user.getMusicPlayer().getPodcast());
                                    }
                                }
                                if (leftTime > 0) {
                                    setTrackName(currentEpisode.getName());
                                    user.getMusicPlayer().setEpisode(currentEpisode);
                                    user.getMusicPlayer().setPlayTimestamp(timestamp);
                                    user.getMusicPlayer().setRemainedTime(leftTime);
                                    user.getMusicPlayer().getEpisode().setRemainingTime(leftTime);
                                    setRemainedTime(leftTime);
                                } else {
                                    setTrackName("");
                                    user.getMusicPlayer().setPaused(true);

                                    setRemainedTime(0);
                                    user.getMusicPlayer().setRemainedTime(0);
                                    user.setSomethingLoaded(false);
                                }
                            }
                        } else if (user.getMusicPlayer().isPaused()) {
                            // The podcast is on pause
                            int leftTime = user.getMusicPlayer().getRemainedTime();
                            int timestamp = command.getTimestamp();
                            if (leftTime > 0) {
                                setRemainedTime(leftTime);
                            } else {
                                // Get the whole podcast and find the next episode where "leftTIme > 0"
                                int index = user.getMusicPlayer().getPodcast().getEpisodes().indexOf(playerEpisode);
                                Episodes currentEpisode = playerEpisode;
                                while(index < user.getMusicPlayer().getPodcast().getEpisodes().size() - 1 && leftTime < 0) {
                                    index++;
                                    currentEpisode = user.getMusicPlayer().getPodcast().getEpisodes().get(index);
                                    leftTime += currentEpisode.getRemainingTime();
                                }
                                // If there is at least one episode with time left
                                if (leftTime > 0) {
                                    setTrackName(currentEpisode.getName());
                                    user.getMusicPlayer().setEpisode(currentEpisode);
                                    user.getMusicPlayer().setPlayTimestamp(timestamp);
                                    user.getMusicPlayer().setRemainedTime(leftTime);
                                    user.getMusicPlayer().getEpisode().setRemainingTime(leftTime);
                                    setRemainedTime(leftTime);
                                } else {
                                    setTrackName("");
                                    user.getMusicPlayer().setPaused(true);

                                    setRemainedTime(0);
                                    user.getMusicPlayer().setRemainedTime(0);
                                }
                            }
                        }
                    }

                    if (user.getMusicPlayer().isPaused())
                        setPaused(true);
                    else
                        setPaused(false);

                    setRepeat(user.getMusicPlayer().getRepeatMode());
                    repeatMessage(user.getMusicPlayer().getRepeatMode(), user.getTrackType());

                    if (user.getMusicPlayer().isShuffled())
                        setShuffle(true);
                    else
                        setShuffle(false);
                } else {
                    // Nothing is loaded in users loader
                    setTrackName("");
                    setPaused(true);
                    setRemainedTime(0);
                    setRepeat(0);
                    repeatMessage(getRepeat(), user.getTrackType());
                    setShuffle(false);
                }
                break;
            }
        }

    }

    // Method used for setting a message according to the status of repeat
    void repeatMessage(int repeatMode, Users.Track track) {
        if (track== Users.Track.PLAYLIST) {
            switch (repeatMode) {
                case 1:
                    setRepeatMessage("Repeat All");
                    break;
                case 2:
                    setRepeatMessage("Repeat Current Song");
                    break;
                default:
                    setRepeatMessage("No Repeat");
                    break;
            }
        } else {
            switch (repeatMode) {
                case 1:
                    setRepeatMessage("Repeat Once");
                    break;
                case 2:
                    setRepeatMessage("Repeat Infinite");
                    break;
                default:
                    setRepeatMessage("No Repeat");
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

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
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

    public String getRepeatMessage() {
        return repeatMessage;
    }

    public void setRepeatMessage(String repeatMessage) {
        this.repeatMessage = repeatMessage;
    }
}
