package commands.searchBar;

import commands.Command;
import lombok.Getter;
import main.Library;
import userEntities.Users;
import userEntities.audio.Episodes;
import userEntities.audio.Songs;

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

        //!!!!!
        if (user == null)
            return;

        if (!user.isSomethingSelected()) {
            setMessage("Please select a source before attempting to load.");
        } else {
            setMessage("Playback loaded successfully.");
            user.setSomethingLoaded(true);
            user.getMusicPlayer().setPlayTimestamp(command.getTimestamp());
            user.getMusicPlayer().setPaused(false);

            // Setup for when a song is played by a user
            if (user.getTrackType() == Users.Track.SONG) {
                Songs playerSong = user.getMusicPlayer().getSong();

                user.getMusicPlayer().setRemainedTime(playerSong.getDuration());
            } else if (user.getTrackType() == Users.Track.PLAYLIST) {
                if (user.getMusicPlayer().getPlaylist().getSongs().isEmpty()) {
                    return;
                }
                // Get the first song from that playlist
                Songs playerSong = user.getMusicPlayer().getPlaylist().getSongs().get(0);

                user.getMusicPlayer().setRemainedTime(playerSong.getDuration());
                // Set the first song on the player
                user.getMusicPlayer().setSong(playerSong);
            } else {
                // Get the first episode from the podcast
                Episodes playerEpisode = user.getMusicPlayer().getPodcast().getEpisodes().get(0);

                user.getMusicPlayer().setRemainedTime(playerEpisode.getDuration());
                // Set the first episode on the player
                user.getMusicPlayer().setEpisode(playerEpisode);
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
}

