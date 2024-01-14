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

@Getter
@Setter
public class CancelPremium extends Command {
    private String message;
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

    private void computeRevenuePerSong(Users user, Library library) {
        int songTotal = user.getSongsFromArtists().values().stream().mapToInt(Integer::intValue).sum();

        for (Map.Entry<Songs, Integer> entry : user.getSongsFromArtists().entrySet()) {
            Songs song = entry.getKey();
            int songsFromArtist = entry.getValue();
            Artist artist = (Artist) user.getUser(library.getUsers(), song.getArtist());

            double revenue = (Math.pow(10, 6) / songTotal) * songsFromArtist;

            artist.getSongsRevenues().merge(song, revenue, Double::sum);
        }
        user.setSongsFromArtists(new HashMap<>());
    }


}
