package commands.users;

import commands.Command;
import lombok.Getter;
import main.Library;
import user.entities.Users;
import user.entities.audio.files.Album;
import user.entities.audio.files.Playlists;

/**
 * Represents a command to shuffle the songs in a playlist.
 */
@Getter
public class Shuffle extends Command {
    private String message;

    /**
     * Shuffles the songs in a playlist based on the specified seed.
     *
     * @param command The command containing user-specific information, timestamp, and request
     *                details.
     * @param library The library containing playlists and user information.
     */
    public void returnShuffle(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());

        Status status = new Status();
        status.returnStatus(command, library);

        // If there is nothing loaded
        if (!user.isSomethingLoaded()) {
            setMessage("Please load a source before using the shuffle function.");
            return;
        }

        // If the loaded source is not a playlist nor an album
        if (!(user.getTrackType() == Users.Track.PLAYLIST
                || user.getTrackType() == Users.Track.ALBUM)) {
            setMessage("The loaded source is not a playlist or an album.");
            return;
        }

        if (user.getTrackType() == Users.Track.PLAYLIST) {
            shufflePlaylist(command, user);
        } else if (user.getTrackType() == Users.Track.ALBUM) {
            shuffleAlbum(command, user);
        }
    }

    private void shufflePlaylist(final Command command, final Users user) {
        final int seed = command.getSeed();
        if (!user.getMusicPlayer().isShuffled()) {
            user.getMusicPlayer().setShuffled(true);

            if (user.getMusicPlayer().getPlaylist() == null) {
                return;
            }

            Playlists originalPlaylist = user.getMusicPlayer().getPlaylist();
            Playlists shuffledPlaylist = new Playlists(originalPlaylist, seed);
            user.getMusicPlayer().setPlaylistsShuffled(shuffledPlaylist);

            // Swap values
            Playlists temp = originalPlaylist;
            originalPlaylist = shuffledPlaylist;
            shuffledPlaylist = temp;

            user.getMusicPlayer().setPlaylist(originalPlaylist);
            user.getMusicPlayer().setPlaylistsShuffled(shuffledPlaylist);

            setMessage("Shuffle function activated successfully.");
        } else {
            user.getMusicPlayer().setShuffled(false);

            // Set the value for playlist to the original playlist
            user.getMusicPlayer().setPlaylist(user.getMusicPlayer().getPlaylistsShuffled());
            setMessage("Shuffle function deactivated successfully.");
        }
    }

    private void shuffleAlbum(final Command command, final Users user) {
        final int seed = command.getSeed();
        if (!user.getMusicPlayer().isShuffled()) {
            user.getMusicPlayer().setShuffled(true);

            Album originalAlbum = user.getMusicPlayer().getAlbum();
            Album shuffledAlbum = new Album(originalAlbum, seed);
            user.getMusicPlayer().setAlbumShuffled(shuffledAlbum);

            // Swap values
            Album temp = originalAlbum;
            originalAlbum = shuffledAlbum;
            shuffledAlbum = temp;

            user.getMusicPlayer().setAlbum(originalAlbum);
            user.getMusicPlayer().setAlbumShuffled(shuffledAlbum);

            setMessage("Shuffle function activated successfully.");
        } else {
            user.getMusicPlayer().setShuffled(false);

            // Set the value for album to the original album
            user.getMusicPlayer().setAlbum(user.getMusicPlayer().getAlbumShuffled());
            setMessage("Shuffle function deactivated successfully.");
        }
    }

    /**
     * Sets the message associated with the execution of the command.
     *
     * @param message The message to be set.
     */
    public void setMessage(final String message) {
        this.message = message;
    }
}
