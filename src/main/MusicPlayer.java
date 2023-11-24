package main;

import lombok.Getter;
import java.util.ArrayList;

/**
 * Represents a music player with information about the current song, playlist,
 * podcast, episode and playback status.
 */
@Getter
public class MusicPlayer {
    private Songs song = null;
    private Playlists playlist = null;

    private Playlists playlistsShuffled = null;
    private ArrayList<Podcasts> podcasts = null;

    private Podcasts podcast = null;
    private Episodes episode = null;

    private int remainedTime;
    private int playTimestamp = -1;

    // Only 3 modes for the repeat command
    private final int maxRepeatMode = 3;
    private int repeatMode = 0;
    private boolean isShuffled = false;
    private boolean isPaused = true;

    /**
     * Sets the current song in the music player.
     *
     * @param song The song to be set.
     */
    public void setSong(final Songs song) {
        this.song = song;
    }

    /**
     * Sets the current playlist in the music player.
     *
     * @param playlist The playlist to be set.
     */
    public void setPlaylist(final Playlists playlist) {
        this.playlist = playlist;
    }

    /**
     * Sets the podcasts in the music player, creating deep copies if necessary.
     *
     * @param podcasts The list of podcasts to be set.
     */
    public void setPodcasts(final ArrayList<Podcasts> podcasts) {
        if (podcasts != null) {
            // Create a new ArrayList for the deep copy
            this.podcasts = new ArrayList<>();

            // Iterate over the original podcasts and create copies
            for (Podcasts originalPodcast : podcasts) {
                this.podcasts.add(new Podcasts(originalPodcast));
            }
        } else {
            this.podcasts = null;
        }
    }

    /**
     * Sets the current podcast in the music player.
     *
     * @param podcast The podcast to be set.
     */
    public void setPodcast(final Podcasts podcast) {
        this.podcast = podcast;
    }

    /**
     * Sets the current episode in the music player.
     *
     * @param episode The episode to be set.
     */
    public void setEpisode(final Episodes episode) {
        this.episode = episode;
    }

    /**
     * Checks if the music player is currently paused.
     *
     * @return True if the player is paused, false otherwise.
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * Sets the playback status of the music player.
     *
     * @param paused True to pause the player, false to resume playback.
     */
    public void setPaused(final boolean paused) {
        isPaused = paused;
    }

    /**
     * Sets the remaining time for the current track in the music player.
     *
     * @param remainedTime The remaining time in seconds.
     */
    public void setRemainedTime(final int remainedTime) {
        this.remainedTime = remainedTime;
    }

    /**
     * Sets the timestamp for the current play position in the music player.
     *
     * @param playTimestamp The timestamp in seconds.
     */
    public void setPlayTimestamp(final int playTimestamp) {
        this.playTimestamp = playTimestamp;
    }

    /**
     * Sets the repeat mode for the music player.
     *
     * @param repeatMode The repeat mode to be set.
     */
    public void setRepeatMode(final int repeatMode) {
        if (repeatMode == maxRepeatMode) {
            this.repeatMode = 0;
            return;
        }
        this.repeatMode = repeatMode;
    }

    /**
     * Sets the shuffled playlists in the music player.
     *
     * @param playlistsShuffled The shuffled playlists to be set.
     */
    public void setPlaylistsShuffled(final Playlists playlistsShuffled) {
        this.playlistsShuffled = playlistsShuffled;
    }

    /**
     * Checks if the music player is currently in shuffle mode.
     *
     * @return True if shuffle mode is active, false otherwise.
     */
    public boolean isShuffled() {
        return isShuffled;
    }

    /**
     * Sets the shuffle mode for the music player.
     *
     * @param shuffled True to activate shuffle mode, false to deactivate.
     */
    public void setShuffled(final boolean shuffled) {
        isShuffled = shuffled;
    }
}

