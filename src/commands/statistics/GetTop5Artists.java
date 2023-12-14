package commands.statistics;

import commands.Command;
import lombok.Getter;
import main.Library;
import user.entities.Artist;
import user.entities.audio.files.Album;
import user.entities.audio.files.Songs;
import user.entities.Users;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * The GetTop5Artists class represents a command to retrieve a list of top artists
 * based on the total number of likes.
 * received by their albums. It extends the Command class.
 */
@Getter
public class GetTop5Artists extends Command {
    private final ArrayList<Artist> topArtist = new ArrayList<>();
    private final int maxArtist = 5;

    /**
     * Retrieves a list of top artists based on the total number of likes received by their albums.
     *
     * @param command The command containing relevant details.
     * @param library The library containing user and album information.
     */
    public void returnGetTop5Artists(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        // Get all albums from the library
        ArrayList<Album> allAlbums = new ArrayList<>(library.getAlbums());
        ArrayList<GetTop5Albums.AlbumLikes> albumLikes = new ArrayList<>();

        for (Album album : allAlbums) {
            int noOfLikes = 0;
            for (Songs songAlbum : album.getSongs()) {
                noOfLikes += songAlbum.getNumberOfLikes();
            }
            GetTop5Albums.AlbumLikes newAlbumLikes = new GetTop5Albums
                                                    .AlbumLikes(album, noOfLikes);
            albumLikes.add(newAlbumLikes);
        }

        ArrayList<Users> allUsers = library.getUsers();
        ArrayList<ArtistLikes> artistLikes = new ArrayList<>();

        // Iterate through all users
        for (Users users : allUsers) {
            int noOfLikes = 0;
            // If it is an artist
            if (users.getUserType() == Users.UserType.ARTIST) {
                // Search through albums and their likes
                for (GetTop5Albums.AlbumLikes artistAlbum : albumLikes) {
                    // If the artist is the owner
                    if (artistAlbum.getAlbum().getOwner().equals(users.getUsername())) {
                        noOfLikes += artistAlbum.getTotalLikes();
                    }
                }
                ArtistLikes newArtistLikes = new ArtistLikes((Artist) users, noOfLikes);
                artistLikes.add(newArtistLikes);
            }
        }

        artistLikes.sort(Comparator
                .comparingInt(GetTop5Artists.ArtistLikes::getTotalLikes).reversed());

        int count = 0;
        for (ArtistLikes artistLikes1 : artistLikes) {
            if (count < maxArtist) {
                topArtist.add(artistLikes1.getArtist());
                count++;
            } else {
                break;
            }
        }
    }

    /**
     * Helper class to store artist and the total number of likes.
     */
    @Getter
    public static class ArtistLikes {
        private final Artist artist;
        private final int totalLikes;

        /**
         * Constructs an ArtistLikes object with the specified artist and total likes.
         *
         * @param artist The artist associated with the likes.
         * @param totalLikes The total number of likes received by the artist.
         */
        public ArtistLikes(final Artist artist, final int totalLikes) {
            this.artist = artist;
            this.totalLikes = totalLikes;
        }
    }
}
