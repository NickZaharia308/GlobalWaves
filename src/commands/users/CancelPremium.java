package commands.users;

import commands.Command;
import lombok.Getter;
import lombok.Setter;
import main.Library;
import user.entities.Artist;
import user.entities.Users;
import user.entities.audio.files.Songs;

import java.util.HashMap;
import java.util.Map;

/**
 * CancelPremium class is used by a user to cancel a premium subscription. (if the user is premium).
 * A message is set to inform the user if the operation was successful or not.
 */
@Getter
@Setter
public class CancelPremium extends Command {
    private String message;
    private final int base = 10;
    private final int exponent = 6;

    /**
     * Cancels a premium subscription for a user.
     * @param command the command to be executed
     * @param library the main library
     */
    public void returnCancelPremium(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());


        if (user == null) {
            setMessage("The username " + command.getUsername() + " doesn't exist.");
            return;
        }

        if (!user.isPremium()) {
            setMessage(command.getUsername() + " is not a premium user.");
            return;
        }

        user.setPremium(false);
        setMessage(command.getUsername() + " cancelled the subscription successfully.");
        computeRevenuePerSong(user, library);
    }

    /**
     * When the user cancels premium, the revenue per song is computed and added to the artist's
     * revenue.
     * The formula is val = (10^6 / totalSongs) * songsFromArtist
     * @param user the user that cancels the premium subscription
     * @param library the main library
     */
    private void computeRevenuePerSong(final Users user, final Library library) {
        int songTotal =
                user.getSongsFromArtists().values().stream().mapToInt(Integer::intValue).sum();

        for (Map.Entry<Songs, Integer> entry : user.getSongsFromArtists().entrySet()) {
            Songs song = entry.getKey();
            int songsFromArtist = entry.getValue();
            Artist artist = (Artist) user.getUser(library.getUsers(), song.getArtist());

            // base is 10, exponent is 6
            double revenue = (Math.pow(base, exponent) / songTotal) * songsFromArtist;

            artist.getSongsRevenues().merge(song, revenue, Double::sum);
        }
        user.setSongsFromArtists(new HashMap<>());
    }


}
