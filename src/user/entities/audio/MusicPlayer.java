package user.entities.audio;

import lombok.Getter;
import lombok.Setter;
import user.entities.audio.files.Album;
import user.entities.audio.files.Episodes;
import user.entities.audio.files.Playlists;
import user.entities.audio.files.Podcasts;
import user.entities.audio.files.Songs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Represents a music player with information about the current song, playlist,
 * podcast or album and playback status.
 */
@Getter
@Setter
public class MusicPlayer {
    private Songs song = null;

    private Playlists playlist = null;
    private Playlists playlistsShuffled = null;

    private Album album = null;
    private Album albumShuffled = null;

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

    private Queue<Songs> trackQueue = new LinkedList<>();
    private Queue<Songs> adQueue = new LinkedList<>();
    private int noOfSongsBreak = 0;

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
     * Adds the song to the track queue.
     *
     * @param songToAdd  The song to be added to the queue.
     * @param queue The queue to which the song is added.
     */
    public void addToTrackQueue(final Songs songToAdd, final Queue<Songs> queue) {
        // Add the song to the trackQueue
        queue.offer(songToAdd);
    }

    /**
     * Adds the songs from the playlist to the track queue.
     *
     * @param playlistParam The playlist from which to add songs to the queue.
     * @param queue    The queue to which the songs are added.
     */
    public void addSongsToQueue(final Playlists playlistParam, final Queue<Songs> queue) {
        if (playlistParam != null) {
            List<Songs> playlistSongs = playlistParam.getSongs();
            for (Songs songInPlaylist : playlistSongs) {
                addToTrackQueue(songInPlaylist, queue);
            }
        }
    }

    /**
     * Adds the songs from the album to the track queue.
     *
     * @param albumParam The album from which to add songs to the queue.
     * @param queue The queue to which the songs are added.
     */
    public void addSongsToQueue(final Album albumParam, final Queue<Songs> queue) {
        if (albumParam != null) {
            List<Songs> albumSongs = albumParam.getSongs();
            for (Songs songInAlbum : albumSongs) {
                addToTrackQueue(songInAlbum, queue);
            }
        }
    }
}
