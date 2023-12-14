package commands.statistics;

import commands.Command;
import lombok.Getter;
import main.Library;
import user.entities.audio.files.Album;
import user.entities.audio.files.Songs;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * The GetTop5Albums class represents a command to retrieve the top 5 albums based on the
 * total number of likes of their songs.
 * It extends the Command class and provides a method to calculate and return the top albums.
 */
@Getter
public class GetTop5Albums extends Command {
    private final ArrayList<Album> topAlbums = new ArrayList<>();
    private final int maxAlbums = 5;

    /**
     * Retrieves the top 5 albums based on the total number of likes for their songs.
     * The albums are sorted in descending order by total likes and album name.
     *
     * @param command The command containing information about the operation.
     * @param library The library containing all albums and their associated songs.
     */
    public void returnGetTop5Albums(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        // Get all albums from the library
        ArrayList<Album> allAlbums = new ArrayList<>(library.getAlbums());
        ArrayList<AlbumLikes> albumLikes = new ArrayList<>();

        for (Album album : allAlbums) {
            int noOfLikes = 0;
            for (Songs songAlbum : album.getSongs()) {
                noOfLikes += songAlbum.getNumberOfLikes();
            }
            AlbumLikes newAlbumLikes = new AlbumLikes(album, noOfLikes);
            albumLikes.add(newAlbumLikes);
        }

        albumLikes.sort(Comparator
                .comparingInt(AlbumLikes::getTotalLikes).reversed()
                .thenComparing(AlbumLikes::getAlbumName));

        int count = 0;
        for (AlbumLikes albumLikes1 : albumLikes) {
            if (count < maxAlbums) {
                topAlbums.add(albumLikes1.getAlbum());
                count++;
            } else {
                break;
            }
        }
    }


    /**
     * Helper class to store album and total likes.
     */
    public static class AlbumLikes {
        private final Album album;
        private final int totalLikes;
        private final String albumName;

        /**
         * Constructs an AlbumLikes object with the specified album and total likes.
         *
         * @param album      The album associated with the likes.
         * @param totalLikes The total number of likes for the album.
         */
        public AlbumLikes(final Album album, final int totalLikes) {
            this.album = album;
            this.totalLikes = totalLikes;
            this.albumName = album.getName();
        }

        /**
         * Gets the associated album.
         *
         * @return The album object.
         */
        public Album getAlbum() {
            return album;
        }

        /**
         * Gets the total number of likes for the album.
         *
         * @return The total number of likes.
         */
        public int getTotalLikes() {
            return totalLikes;
        }

        /**
         * Gets the name of the album.
         *
         * @return The name of the album.
         */
        public String getAlbumName() {
            return albumName;
        }
    }
}
