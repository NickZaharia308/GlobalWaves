package commands.users;

import commands.Command;
import commands.searchBar.Load;
import lombok.Getter;
import main.Library;
import user.entities.Artist;
import user.entities.Users;
import user.entities.audio.files.Episodes;
import user.entities.audio.files.Songs;

import java.util.LinkedList;
import java.util.Queue;

@Getter
/**
 * The `Status` class represents the current status of the music player, including
 * track information, remaining time, shuffle status, pause status, repeat mode, and repeat message.
 * It extends the `Command` class.
 *
 */
public class Status extends Command {
    private String trackName;
    private int remainedTime;
    private boolean shuffle, paused;
    private int repeat;
    private String repeatMessage;

    /**
     * Retrieves and sets the status based on the provided command and library.
     * It is used for other commands such as next or prev (to go to another songs).
     * It is also used to update the current track, being the only class that passes through
     * the songs of a playlist or episodes of a podcast, accounting for repeat, shuffle and others
     *
     * @param command The command containing user information.
     * @param library The library containing user and music player information.
     */
    public void returnStatus(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());


        if (!user.isOnline() && user.isSomethingLoaded()) {

            if (user.getMusicPlayer() == null) {
                return;
            }

            if (user.getTrackType() == Users.Track.SONG || user.getTrackType()
                    == Users.Track.PLAYLIST || user.getTrackType() == Users.Track.ALBUM) {
                if (user.getMusicPlayer().getSong() == null) {
                    return;
                }

                // Set song
                Songs playerSong = user.getMusicPlayer().getSong();
                setTrackName(playerSong.getName());

                setRemainedTime(user.getMusicPlayer().getRemainedTime());
            } else {
                if (user.getMusicPlayer().getEpisode() == null) {
                    return;
                }

                // Set podcast
                Episodes playerEpisode = user.getMusicPlayer().getEpisode();
                setTrackName(playerEpisode.getName());

                setRemainedTime(user.getMusicPlayer().getRemainedTime());
            }

            setPaused(user.getMusicPlayer().isPaused());
            setShuffle(user.getMusicPlayer().isShuffled());
            setRepeat(user.getMusicPlayer().getRepeatMode());
            repeatMessage(getRepeat(), user.getTrackType());

        } else if (user.isSomethingLoaded() && user.getMusicPlayer() != null) {
            if (user.getTrackType() == Users.Track.SONG) {
                Songs playerSong = user.getMusicPlayer().getSong();
                if (!user.getMusicPlayer().getTrackQueue().isEmpty() &&
                    playerSong.getName().equals(user.getMusicPlayer().getTrackQueue().peek().getName())) {

                    playerSong = user.getMusicPlayer().getTrackQueue().poll();
                    if (playerSong.getName().equals("Ad Break")) {
                        computeRevenuePerSongFromQueue(user, library, command);
                    } else if (!user.isPremium()) {
                        user.getMusicPlayer().getAdQueue().offer(playerSong);
                        user.getMusicPlayer().setNoOfSongsBreak(user.getMusicPlayer().getNoOfSongsBreak() + 1);
                    }
                }


                if (user.getMusicPlayer().getSong() == null) {
                    return;
                }


                setTrackName(playerSong.getName());
                if (user.getMusicPlayer().getPlayTimestamp() == -1) {
                    setRemainedTime(user.getMusicPlayer().getRemainedTime());
                } else if (!user.getMusicPlayer().isPaused()) {
                    // Computing the remaining time for a track
                    int playTimestamp = user.getMusicPlayer().getPlayTimestamp();
                    int timestamp = command.getTimestamp();
                    int leftTime = user.getMusicPlayer().getRemainedTime() + playTimestamp
                                    - timestamp;

                    // If the remaining time > 0, set the value to it, else set the value to 0
                    if (leftTime > 0) {
                        user.getMusicPlayer().setPlayTimestamp(timestamp);
                        user.getMusicPlayer().setRemainedTime(leftTime);
                        setRemainedTime(leftTime);
                    } else {

                        Songs currentSong;
                        user.setLastSong(playerSong.getName());

                        if (!user.getMusicPlayer().getTrackQueue().isEmpty()) {
                            currentSong = user.getMusicPlayer().getTrackQueue().poll();

                            if (currentSong.getName().equals("Ad Break")) {
                                command.setPrice(currentSong.getAdPrice());
                                computeRevenuePerSongFromQueue(user, library, command);
                            } else if (!user.isPremium()) {
                                user.getMusicPlayer().getAdQueue().offer(playerSong);
                                user.getMusicPlayer().setNoOfSongsBreak(user.getMusicPlayer().getNoOfSongsBreak() + 1);
                            }
                        }

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

                if (user.getMusicPlayer().getSong() == null) {
                    return;
                }
                setTrackName(playerSong.getName());
                if (user.getMusicPlayer().getPlayTimestamp() == -1) {
                    setRemainedTime(user.getMusicPlayer().getRemainedTime());
                } else if (!user.getMusicPlayer().isPaused()) {
                    // Computing the remaining time for a track
                    int playTimestamp = user.getMusicPlayer().getPlayTimestamp();
                    int timestamp = command.getTimestamp();
                    int leftTime = user.getMusicPlayer().getRemainedTime() + playTimestamp
                            - timestamp;

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

                            repeatMessage(user.getMusicPlayer().getRepeatMode(),
                                        user.getTrackType());
                        }

                        int index = user.getMusicPlayer().getPlaylist().getSongs().
                                indexOf(playerSong);
                        Songs currentSong = playerSong;
                        while (index < user.getMusicPlayer().getPlaylist().getSongs().size() - 1
                                && leftTime <= 0) {
                            index++;
                            currentSong = user.getMusicPlayer().getPlaylist().getSongs()
                                    .get(index);
                            leftTime += currentSong.getDuration();

                            // if the repeat mode is "repeat all and we reached the last song
                            if (user.getMusicPlayer().getRepeatMode() == 1 && leftTime <= 0
                                && index == user.getMusicPlayer().getPlaylist().getSongs().size()
                                    - 1) {
                                index = -1;
                            }
                        }
                        // if the repeat mode is "repeat all and we reached the last song
                        if (user.getMusicPlayer().getRepeatMode() == 1 && leftTime <= 0
                                && index == user.getMusicPlayer().getPlaylist().getSongs().
                                size() - 1) {
                            currentSong = user.getMusicPlayer().getPlaylist().getSongs().get(0);
                            leftTime += currentSong.getDuration();
                            index = -1;

                            // Repeat the songs
                            while (index < user.getMusicPlayer().getPlaylist().getSongs().size()
                                    - 1 && leftTime <= 0) {
                                index++;
                                currentSong = user.getMusicPlayer().getPlaylist().getSongs()
                                        .get(index);
                                leftTime += currentSong.getDuration();

                                // if the repeat mode is "repeat all and we reached the last song
                                if (user.getMusicPlayer().getRepeatMode() == 1 && leftTime <= 0
                                        && index == user.getMusicPlayer().getPlaylist()
                                        .getSongs().size() - 1) {
                                    index = -1;
                                }
                            }
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
                            user.getMusicPlayer().setShuffled(false);

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
                            while (leftTime <= 0) {
                                leftTime += playerSong.getDuration();
                            }
                            setTrackName(playerSong.getName());
                            user.getMusicPlayer().setSong(playerSong);
                            user.getMusicPlayer().setPlayTimestamp(timestamp);
                            user.getMusicPlayer().setRemainedTime(leftTime);
                            setRemainedTime(leftTime);
                            setPaused(false);

                            repeatMessage(user.getMusicPlayer().getRepeatMode(),
                                          user.getTrackType());
                        }


                        int index = user.getMusicPlayer().getPlaylist().getSongs().
                                indexOf(playerSong);
                        Songs currentSong = playerSong;
                        while (index < user.getMusicPlayer().getPlaylist().getSongs().size() - 1
                                    && leftTime <= 0) {
                            index++;
                            currentSong = user.getMusicPlayer().getPlaylist().getSongs()
                                    .get(index);
                            leftTime += currentSong.getDuration();

                            // if the repeat mode is "repeat all and we reached the last song
                            if (user.getMusicPlayer().getRepeatMode() == 1 && leftTime < 0
                                    && index == user.getMusicPlayer().getPlaylist().getSongs()
                                    .size() - 1) {
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
            } else if (user.getTrackType() == Users.Track.PODCAST) {
                // Podcast case
                Episodes playerEpisode = user.getMusicPlayer().getEpisode();

                if (user.getMusicPlayer().getEpisode() == null) {
                    return;
                }
                setTrackName(playerEpisode.getName());
                if (user.getMusicPlayer().getPlayTimestamp() == -1) {
                    setRemainedTime(user.getMusicPlayer().getRemainedTime());
                } else if (!user.getMusicPlayer().isPaused()) {
                    // Computing the remaining time for a track
                    int playTimestamp = user.getMusicPlayer().getPlayTimestamp();
                    int timestamp = command.getTimestamp();
                    int leftTime = user.getMusicPlayer().getEpisode().getRemainingTime()
                            + playTimestamp - timestamp;

                    // If the remaining time > 0, set the value to it, else set the value to 0
                    if (leftTime > 0) {
                        user.getMusicPlayer().setPlayTimestamp(timestamp);
                        user.getMusicPlayer().setRemainedTime(leftTime);
                        user.getMusicPlayer().getEpisode().setRemainingTime(leftTime);
                        setRemainedTime(leftTime);
                    } else {
                        // Get the whole podcast and find the next episode where "leftTIme > 0"
                        int index = user.getMusicPlayer().getPodcast().getEpisodes().
                                indexOf(playerEpisode);
                        Episodes currentEpisode = playerEpisode;
                        while (index < user.getMusicPlayer().getPodcast().getEpisodes().size() - 1
                                && leftTime <= 0) {
                            index++;
                            currentEpisode = user.getMusicPlayer().getPodcast().getEpisodes().
                                    get(index);
                            leftTime += currentEpisode.getRemainingTime();

                            // If all the episodes finished, reset the episodes
                            if (index == user.getMusicPlayer().getPodcast().getEpisodes().size()
                                    - 1) {
                                user.getMusicPlayer().getPodcast().resetEpisodes(user.
                                        getMusicPlayer().getPodcast());
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
                        int index = user.getMusicPlayer().getPodcast().getEpisodes()
                                .indexOf(playerEpisode);
                        Episodes currentEpisode = playerEpisode;
                        while (index < user.getMusicPlayer().getPodcast().getEpisodes().size() - 1
                                        && leftTime < 0) {
                            index++;
                            currentEpisode = user.getMusicPlayer().getPodcast().getEpisodes()
                                    .get(index);
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
            } else if (user.getTrackType() == Users.Track.ALBUM) {
                Songs playerSong = user.getMusicPlayer().getSong();

                if (playerSong == null) {
                    return;
                }
                if (!user.getMusicPlayer().getTrackQueue().isEmpty() &&
                        playerSong.getName().equals(user.getMusicPlayer().getTrackQueue().peek().getName())) {

                    playerSong = user.getMusicPlayer().getTrackQueue().poll();
                    if (playerSong.getName().equals("Ad Break")) {
                        computeRevenuePerSongFromQueue(user, library, command);
                    } else if (!user.isPremium()) {
                        user.getMusicPlayer().getAdQueue().offer(playerSong);
                        user.getMusicPlayer().setNoOfSongsBreak(user.getMusicPlayer().getNoOfSongsBreak() + 1);
                    }
                }
                setTrackName(playerSong.getName());
                if (user.getMusicPlayer().getPlayTimestamp() == -1) {
                    setRemainedTime(user.getMusicPlayer().getRemainedTime());
                } else if (!user.getMusicPlayer().isPaused()) {
                    // Computing the remaining time for a track
                    int playTimestamp = user.getMusicPlayer().getPlayTimestamp();
                    int timestamp = command.getTimestamp();
                    int leftTime = user.getMusicPlayer().getRemainedTime() + playTimestamp
                            - timestamp;

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

                            repeatMessage(user.getMusicPlayer().getRepeatMode(),
                                    user.getTrackType());
                        }

                        int index = user.getMusicPlayer().getAlbum().getSongs().
                                indexOf(playerSong);
                        Songs currentSong = playerSong;

                        while (!user.getMusicPlayer().getTrackQueue().isEmpty() && leftTime <= 0) {
                            currentSong = user.getMusicPlayer().getTrackQueue().poll();
                            if (currentSong.getName().equals("Ad Break")) {
                                computeRevenuePerSongFromQueue(user, library, command);
                            } else if (!user.isPremium()) {
                                user.getMusicPlayer().getAdQueue().offer(currentSong);
                                user.getMusicPlayer().setNoOfSongsBreak(user.getMusicPlayer().getNoOfSongsBreak() + 1);
                            }
                            leftTime += currentSong.getDuration();

                            // if the repeat mode is "repeat all and we reached the last song
                            if (user.getMusicPlayer().getRepeatMode() == 1 && leftTime <= 0
                                    && user.getMusicPlayer().getTrackQueue().isEmpty()) {
                                user.getMusicPlayer().addSongsToQueue(user.getMusicPlayer().getAlbum(), user.getMusicPlayer().getTrackQueue());
                            }
                            user.setLastSong(currentSong.getName());
                        }
                        // if the repeat mode is "repeat all and we reached the last song
                        if (user.getMusicPlayer().getRepeatMode() == 1 && leftTime <= 0
                                && index == user.getMusicPlayer().getAlbum().getSongs().
                                size() - 1) {
                            currentSong = user.getMusicPlayer().getAlbum().getSongs().get(0);
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
                            user.getMusicPlayer().setShuffled(false);

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

                            repeatMessage(user.getMusicPlayer().getRepeatMode(),
                                    user.getTrackType());
                        }

                        int index = user.getMusicPlayer().getAlbum().getSongs().
                                indexOf(playerSong);
                        Songs currentSong = playerSong;
                        while (index < user.getMusicPlayer().getAlbum().getSongs().size() - 1
                                && leftTime <= 0) {
                            index++;
                            currentSong = user.getMusicPlayer().getAlbum().getSongs()
                                    .get(index);
                            leftTime += currentSong.getDuration();

                            // if the repeat mode is "repeat all and we reached the last song
                            if (user.getMusicPlayer().getRepeatMode() == 1 && leftTime < 0
                                    && index == user.getMusicPlayer().getAlbum().getSongs()
                                    .size() - 1) {
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
            }

            if (user.getMusicPlayer().isPaused()) {
                setPaused(true);
            } else {
                setPaused(false);
            }

            setRepeat(user.getMusicPlayer().getRepeatMode());
            repeatMessage(user.getMusicPlayer().getRepeatMode(), user.getTrackType());

            if (user.getMusicPlayer().isShuffled()) {
                setShuffle(true);
            } else {
                setShuffle(false);
            }
        } else {
            // Nothing is loaded in users loader
            setTrackName("");
            setPaused(true);
            setRemainedTime(0);
            setRepeat(0);
            repeatMessage(getRepeat(), user.getTrackType());
            setShuffle(false);
        }

        user.setLastSong(trackName);
    }

    /**
     * Sets the message corresponding to the current repeat mode based on the track type.
     *
     * @param repeatMode The current repeat mode.
     * @param track      The current track type (song, playlist, or podcast).
     */
    void repeatMessage(final int repeatMode, final Users.Track track) {
        if (track == Users.Track.PLAYLIST || track == Users.Track.ALBUM) {
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

    /**
     * Sets the name of the current track.
     *
     * @param trackName The name of the current track.
     */
    public void setTrackName(final String trackName) {
        this.trackName = trackName;
    }

    /**
     * Sets the remaining time of the current track.
     *
     * @param remainedTime The remaining time of the current track.
     */
    public void setRemainedTime(final int remainedTime) {
        this.remainedTime = remainedTime;
    }

    /**
     * Sets the repeat mode of the music player.
     *
     * @param repeat The repeat mode of the music player.
     */
    public void setRepeat(final int repeat) {
        this.repeat = repeat;
    }

    /**
     * Sets the shuffle status of the music player.
     *
     * @param shuffle Indicates whether the music player is in shuffle mode.
     */
    public void setShuffle(final boolean shuffle) {
        this.shuffle = shuffle;
    }

    /**
     * Sets the pause status of the music player.
     *
     * @param paused Indicates whether the music player is paused.
     */
    public void setPaused(final boolean paused) {
        this.paused = paused;
    }

    /**
     * Sets the message corresponding to the current repeat mode.
     *
     * @param repeatMessage The message corresponding to the current repeat mode.
     */
    public void setRepeatMessage(final String repeatMessage) {
        this.repeatMessage = repeatMessage;
    }

    private void computeRevenuePerSongFromQueue(Users user, Library library, Command command) {
        Queue<Songs> adQueue = user.getMusicPlayer().getAdQueue();
        int songTotal = user.getMusicPlayer().getNoOfSongsBreak();
        int numberOfPolledSongs = 0;

        while (!adQueue.isEmpty()) {
            Songs song = adQueue.poll();
            numberOfPolledSongs++;

            Artist artist = (Artist) user.getUser(library.getUsers(), song.getArtist());
            double revenue = (double) command.getPrice() / songTotal;

            artist.getSongsRevenues().merge(song, revenue, Double::sum);
            if (numberOfPolledSongs == songTotal) {
                user.getMusicPlayer().setAdQueue(new LinkedList<>());
                break;
            }
        }
        user.getMusicPlayer().setNoOfSongsBreak(0);
    }
}
