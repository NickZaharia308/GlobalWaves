package commands.admin;

import commands.Command;
import lombok.Getter;
import main.Library;
import user.entities.Artist;
import user.entities.Users;

import java.util.ArrayList;
import java.util.Comparator;

@Getter
public class EndProgram extends Command {
    private ArrayList<Artist> platformArtists = new ArrayList<>();

    public void returnEndProgram(final Command command, final Library library) {
        super.setCommand(command.getCommand());

        for (Users user : library.getUsers()) {
            if (user.getUserType() == Users.UserType.ARTIST) {
                this.platformArtists.add((Artist) user);
            }
        }
        sortArtists(this.platformArtists);
        int ranking = 1;
        for (Artist artist : this.platformArtists) {
            if (artist.hasTrueValue()) {
                artist.setRanking(ranking);
                ranking++;
            }
        }
    }

    public static void sortArtists(ArrayList<Artist> artists) {
        Comparator<Artist> artistComparator = Comparator
                .comparingDouble(Artist::getSongRevenue)
                .thenComparing(Comparator.comparing(Artist::getUsername));

        artists.sort(artistComparator);
    }

}
