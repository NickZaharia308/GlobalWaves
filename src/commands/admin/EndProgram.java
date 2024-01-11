package commands.admin;

import commands.Command;
import commands.users.CancelPremium;
import lombok.Getter;
import main.Library;
import user.entities.Artist;
import user.entities.Users;
import user.entities.audio.files.Songs;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

@Getter
public class EndProgram extends Command {
    private ArrayList<Artist> platformArtists = new ArrayList<>();

    public void returnEndProgram(final Command command, final Library library) {
        super.setCommand(command.getCommand());

        for (Users user : library.getUsers()) {
            if (user.getUserType() == Users.UserType.ARTIST) {
                this.platformArtists.add((Artist) user);
            } else if (user.getUserType() == Users.UserType.NORMAL) {
                // CancelPremium for all users
                command.setUsername(user.getUsername());
                CancelPremium cancelPremium = new CancelPremium();
                cancelPremium.returnCancelPremium(command, library);
            }
        }

        for (Artist artist : this.platformArtists) {
            getRevenueForArtist(artist);
            setSongWithHighestRevenue(artist);
        }

        sortArtists();
        int ranking = 1;
        for (Artist artist : this.platformArtists) {
            if (artist.hasTrueValue()) {
                artist.setRanking(ranking);
                ranking++;
            }
        }
    }

    private void sortArtists() {
        Comparator<Artist> artistComparator = Comparator
                .comparingDouble((Artist artist) -> artist.getSongRevenue())
                .reversed()
                .thenComparing(Artist::getUsername);

        this.platformArtists.sort(artistComparator);
    }


    private void getRevenueForArtist(Artist artist) {

        double revenue = 0;

        for (Map.Entry<Songs, Double> entry : artist.getSongsRevenues().entrySet()) {
            revenue += entry.getValue();
        }
        double songsRevenue = Math.round(revenue * 100.0) / 100.0;
        artist.setSongRevenue(songsRevenue);
    }

    private void setSongWithHighestRevenue(Artist artist) {
        Songs highestRevenueSong = null;
        double maxRevenue = Double.MIN_VALUE;

        for (Map.Entry<Songs, Double> entry : artist.getSongsRevenues().entrySet()) {
            double currentRevenue = entry.getValue();

            if (currentRevenue > maxRevenue) {
                maxRevenue = currentRevenue;
                highestRevenueSong = entry.getKey();
            }
        }
        if (highestRevenueSong != null) {
            artist.setMostProfitableSong(highestRevenueSong.getName());
        }
    }

}
