package commands.page;

import commands.Command;
import lombok.Getter;
import main.Library;
import userEntities.Users;
import userEntities.audio.Playlists;
import userEntities.audio.Songs;

import java.util.ArrayList;

@Getter
public class PrintCurrentPage extends Command {
    private String message;
    private final int maxShowed = 5;

    public void returnPrintCurrentPage(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        Users user = new Users();
        user = user.getUser(library.getUsers(), this.getUsername());

        ArrayList<Songs> likedSongs = user.getLikedSongs();
        ArrayList<Playlists> followedPlaylists = user.getFollowedPlaylists();

        final int maxSongs = Math.min(likedSongs.size(), maxShowed);
        final int maxPlaylists = Math.min(followedPlaylists.size(), maxShowed);

        StringBuilder messageBuilder = new StringBuilder("Liked songs:\n\t[");
        for (int i = 0; i < maxSongs; i++) {
            messageBuilder.append(likedSongs.get(i).getName());

            // Add a comma if it's not the last element
            if (i < maxSongs - 1) {
                messageBuilder.append(", ");
            }
        }
        messageBuilder.append("]\n\nFollowed playlists:\n\t[");
        for (int i = 0; i < maxPlaylists; i++) {
            messageBuilder.append(followedPlaylists.get(i).getName());

            // Add a comma if it's not the last element
            if (i < maxPlaylists - 1) {
                messageBuilder.append(", ");
            }
        }
        messageBuilder.append("]");

        setMessage(messageBuilder.toString());

    }

    public void setMessage(String message) {
        this.message = message;
    }
}
