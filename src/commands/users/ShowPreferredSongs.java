package commands.users;

import commands.Command;
import lombok.Getter;
import main.Library;
import userEntities.Users;
import userEntities.audio.Songs;

import java.util.ArrayList;

/**
 * The {@code ShowPreferredSongs} class represents a command to display a user's liked songs.
 * It extends the {@code Command} class and includes a list of liked songs.
 */
@Getter
public class ShowPreferredSongs extends Command {
    private ArrayList<Songs> likedSongs = null;

    /**
     * Sets its field {@code likedSongs} as the retrieved ArrayList of a user's liked songs
     *
     * @param command The command containing user information.
     * @param library The library containing user and song information.
     */
    public void returnShowPreferredSongs(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());

        setLikedSongs(new ArrayList<>(user.getLikedSongs()));
    }

    /**
     * Sets the list of liked songs for the current instance.
     *
     * @param likedSongs The list of liked songs to be set.
     */
    public void setLikedSongs(final ArrayList<Songs> likedSongs) {
        this.likedSongs = likedSongs;
    }
}
