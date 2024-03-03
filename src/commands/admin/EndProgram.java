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

/**
 * The EndProgram class represents the implicit command given at the end of the program
 * that computes the revenue for each artist, as well as the song with the
 * highest revenue for each artist.
 */
@Getter
public class EndProgram extends Command {
    private ArrayList<Artist> platformArtists = new ArrayList<>();
    private final double percent = 100;

    /**
     * Computes the revenue for each artist and the song with the highest revenue
     * for each artist, as well as the ranking of the artists based on their revenue.
     *
     * @param command The command containing information about the end of the program.
     * @param library The main library containing user data.
     */
    public void returnEndProgram(final Command command, final Library library) {
        super.setCommand(command.getCommand());

        //
        for (Users user : library.getUsers()) {
            if (user.getUserType() == Users.UserType.ARTIST) {
                this.platformArtists.add((Artist) user);
            } else if (user.getUserType() == Users.UserType.NORMAL) {
                command.setUsername(user.getUsername());

                // CancelPremium for all users
                CancelPremium cancelPremium = new CancelPremium();
                cancelPremium.returnCancelPremium(command, library);
            }
        }

        // Compute revenue for each artist
        for (Artist artist : this.platformArtists) {
            getRevenueForArtist(artist);
            setSongWithHighestRevenue(artist);
        }

        // Sort artists based on their revenue
        sortArtists();
        int ranking = 1;
        for (Artist artist : this.platformArtists) {
            if (artist.hasTrueValueInListeners() || artist.getMerchRevenue() > 0) {
                artist.setRanking(ranking);
                ranking++;
            }
        }
    }


    /**
     * Sorts the artists based on their revenue and username.
     */
    private void sortArtists() {
        Comparator<Artist> artistComparator = Comparator
                .comparingDouble((Artist artist) ->
                        artist.getSongRevenue() + artist.getMerchRevenue())
                .reversed()
                .thenComparing(Artist::getUsername);

        this.platformArtists.sort(artistComparator);
    }



    /**
     * Computes the revenue for a given artist based on the songs' revenue.
     *
     * @param artist The artist for which the revenue is computed.
     */
    private void getRevenueForArtist(final Artist artist) {
        double revenue = 0;

        for (Map.Entry<Songs, Double> entry : artist.getSongsRevenues().entrySet()) {
            revenue += entry.getValue();
        }
        double songsRevenue = Math.round(revenue * percent) / percent;
        artist.setSongRevenue(songsRevenue);
    }

    /**
     * Sets the song with the highest revenue for a given artist.
     *
     * @param artist The artist for which the song with the highest revenue is set.
     */
    private void setSongWithHighestRevenue(final Artist artist) {
        Songs highestRevenueSong = null;
        double maxRevenue = Double.MIN_VALUE;

        for (Map.Entry<Songs, Double> entry : artist.getSongsRevenues().entrySet()) {
            double currentRevenue = entry.getValue();

            if (currentRevenue > maxRevenue) {
                maxRevenue = currentRevenue;
                highestRevenueSong = entry.getKey();
            } else if (currentRevenue == maxRevenue) {
                // If two songs have the same revenue, compare their names lexicographically
                String currentSongName = entry.getKey().getName();
                String currentHighestSongName = highestRevenueSong.getName();

                if (currentSongName.compareTo(currentHighestSongName) < 0) {
                    highestRevenueSong = entry.getKey();
                }
            }
        }
        if (highestRevenueSong != null) {
            artist.setMostProfitableSong(highestRevenueSong.getName());
        }
    }
}
