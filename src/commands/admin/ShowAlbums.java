package commands.admin;

import commands.Command;
import main.Library;
import userEntities.Artist;
import userEntities.Users;
import userEntities.audio.Album;

import java.util.ArrayList;

public class ShowAlbums extends Command {
    public ArrayList<Album> returnShowAlbum(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        Users user = new Users();
        user = user.getUser(library.getUsers(), this.getUsername());
        Artist artist = (Artist) user;
        return artist.getAlbums();
    }
}
