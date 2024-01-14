package commands.searchBar;

import commands.Command;
import commands.users.Status;
import lombok.Getter;
import main.Library;
import user.entities.Artist;
import user.entities.Users;
import user.entities.audio.files.Episodes;
import user.entities.audio.files.Songs;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents a load operation based on the provided command and updates the playback status.
 * The load operation depends on the user's previous selection, and it can load a song, playlist,
 * or podcast.
 */
@Getter
public class Load extends Command {
    private String message;

    /**
     * Performs a load operation based on the provided command and updates the playback status.
     * The load operation depends on the user's previous selection, and it can load a song,
     * playlist, or podcast.
     *
     * @param command The load command containing user-specific information and timestamp.
     * @param library The library containing songs, playlists, podcasts, and user information.
     */
    public void returnLoad(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());

        if (!user.isSomethingSelected()) {
            setMessage("Please select a source before attempting to load.");
        } else {
            Status status = new Status();
            status.returnStatus(command, library);

            setMessage("Playback loaded successfully.");
            user.setSomethingLoaded(true);
            user.getMusicPlayer().setPlayTimestamp(command.getTimestamp());
            user.getMusicPlayer().setPaused(false);
            user.getMusicPlayer().setShuffled(false);
            user.getMusicPlayer().setTrackQueue(new LinkedList<>());


            // Setup for when a song is played by a user
            if (user.getTrackType() == Users.Track.SONG) {
                Songs playerSong = user.getMusicPlayer().getSong();

                user.getMusicPlayer().setRemainedTime(playerSong.getDuration());
                user.getMusicPlayer().addToTrackQueue(playerSong, user.getMusicPlayer().getTrackQueue());
                // Update the maps for the user
                updateMaps(playerSong, user, library);
            } else if (user.getTrackType() == Users.Track.PLAYLIST) {
                if (user.getMusicPlayer().getPlaylist().getSongs().isEmpty()) {
                    return;
                }
                // Get the first song from that playlist
                Songs playerSong = user.getMusicPlayer().getPlaylist().getSongs().get(0);

                user.getMusicPlayer().setRemainedTime(playerSong.getDuration());
                user.getMusicPlayer().addSongsToQueue(user.getMusicPlayer().getPlaylist(), user.getMusicPlayer().getTrackQueue());

                // Set the first song on the player
                user.getMusicPlayer().setSong(playerSong);
                // Update the maps for the user
                updateMaps(playerSong, user, library);
            } else if (user.getTrackType() == Users.Track.PODCAST) {
                // Get the first episode from the podcast
                Episodes playerEpisode = user.getMusicPlayer().getPodcast().getEpisodes().get(0);

                user.getMusicPlayer().setRemainedTime(playerEpisode.getDuration());
                // Set the first episode on the player
                user.getMusicPlayer().setEpisode(playerEpisode);
                // Update the map for the episodes
                updateTopEpisodes(playerEpisode, user);
            } else if (user.getTrackType() == Users.Track.ALBUM) {
                if (user.getMusicPlayer().getAlbum().getSongs().isEmpty()) {
                    return;
                }
                // Get the first song from that album
                Songs playerSong = user.getMusicPlayer().getAlbum().getSongs().get(0);

                user.getMusicPlayer().setRemainedTime(playerSong.getDuration());
                user.getMusicPlayer().addSongsToQueue(user.getMusicPlayer().getAlbum(), user.getMusicPlayer().getTrackQueue());

                // Set the first song on the player
                user.getMusicPlayer().setSong(playerSong);
                // Update the maps for the user
                updateMaps(playerSong, user, library);
            }
            user.setSomethingSelected(false);
            user.getMusicPlayer().setRepeatMode(0);
        }
    }

    /**
     * Gets the message generated during the load operation.
     *
     * @return The load operation message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the load operation message.
     *
     * @param message The load operation message to be set.
     */
    public void setMessage(final String message) {
        this.message = message;
    }

    public void updateMaps(final Songs loadedSong, final Users user, final Library library) {
        user.getTopSongs().put(loadedSong.getName(), user.getTopSongs().getOrDefault(loadedSong.getName(), 0) + 1);
        user.getTopGenres().put(loadedSong.getGenre(), user.getTopGenres().getOrDefault(loadedSong.getGenre(), 0) + 1);
        user.getTopArtists().put(loadedSong.getArtist(), user.getTopArtists().getOrDefault(loadedSong.getArtist(), 0) + 1);
        user.getTopAlbums().put(loadedSong.getAlbum(), user.getTopAlbums().getOrDefault(loadedSong.getAlbum(), 0) + 1);

        if (user.isPremium()) {
            user.getSongsFromArtists().put(loadedSong, user.getSongsFromArtists().getOrDefault(loadedSong, 0) + 1);
        }

        Users artistAsUser = user.getUser(library.getUsers(), loadedSong.getArtist());
        artistAsUser.getTopSongs().put(loadedSong.getName(), artistAsUser.getTopSongs().getOrDefault(loadedSong.getName(), 0) + 1);
        artistAsUser.getTopAlbums().put(loadedSong.getAlbum(), artistAsUser.getTopAlbums().getOrDefault(loadedSong.getAlbum(), 0) + 1);
        // Cast to Artist
        Artist artist = (Artist) artistAsUser;
        artist.getListeners().put(user.getUsername(), true);
        artist.getTopFans().put(user.getUsername(), artist.getTopFans().getOrDefault(user.getUsername(), 0) + 1);
    }

    public void updateTopEpisodes(final Episodes loadedEpisode, final Users user) {
        user.getTopEpisodes().put(loadedEpisode.getName(), user.getTopEpisodes().getOrDefault(loadedEpisode.getName(), 0) + 1);
    }
}

