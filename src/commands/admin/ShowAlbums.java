package commands.admin;

import commands.Command;
import main.Library;
import user.entities.Artist;
import user.entities.Users;
import user.entities.audio.files.Album;

import java.util.ArrayList;

/**
 * The ShowAlbums class represents a command to retrieve the albums of a specific artist.
 */
public class ShowAlbums extends Command {

    /**
     * Retrieves and returns the albums of a specific artist.
     *
     * @param command The command containing information about the request.
     * @param library The main library containing user and artist data.
     * @return An ArrayList of albums owned by the specified artist.
     */
    public ArrayList<Album> returnShowAlbum(final Command command, final Library library) {
        // Set command-related information
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        // Get the user from the library based on the provided username
        Users user = new Users();
        user = user.getUser(library.getUsers(), this.getUsername());
        Artist artist = (Artist) user;
        return artist.getAlbums();
    }
}
