package main;

import java.util.ArrayList;

public class MusicPlayer {
    private Songs song = null;
    private Playlists playlist = null;

    private Playlists playlistsShuffled = null;
    private ArrayList<Podcasts> podcasts = null;

    private Podcasts podcast = null;
    private Episodes episode = null;
    boolean isPaused = true;
    private int remainedTime;
    private int playTimestamp = -1;
    private int repeatMode = 0;

    private boolean isShuffled = false;

    public Songs getSong() {
        return song;
    }

    public void setSong(Songs song) {
        this.song = song;
    }

    public Playlists getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlists playlist) {
        this.playlist = playlist;
    }

    public ArrayList<Podcasts> getPodcasts() {
        return podcasts;
    }

    public void setPodcasts(ArrayList<Podcasts> podcasts) {
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

    public Podcasts getPodcast() {
        return podcast;
    }

    public void setPodcast(Podcasts podcast) {
        this.podcast = podcast;
    }

    public Episodes getEpisode() {
        return episode;
    }

    public void setEpisode(Episodes episode) {
        this.episode = episode;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public int getRemainedTime() {
        return remainedTime;
    }

    public void setRemainedTime(int remainedTime) {
        this.remainedTime = remainedTime;
    }

    public int getPlayTimestamp() {
        return playTimestamp;
    }

    public void setPlayTimestamp(int playTimestamp) {
        this.playTimestamp = playTimestamp;
    }

    public int getRepeatMode() {
        return repeatMode;
    }

    public void setRepeatMode(int repeatMode) {
        if (repeatMode == 3) {
            this.repeatMode = 0;
            return;
        }
        this.repeatMode = repeatMode;
    }

    public Playlists getPlaylistsShuffled() {
        return playlistsShuffled;
    }

    public void setPlaylistsShuffled(Playlists playlistsShuffled) {
        this.playlistsShuffled = playlistsShuffled;
    }

    public boolean isShuffled() {
        return isShuffled;
    }

    public void setShuffled(boolean shuffled) {
        isShuffled = shuffled;
    }
}
