package commands.statistics;

import commands.Command;
import lombok.Getter;
import main.Library;
import userEntities.audio.Album;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class GetTop5Albums extends Command {
    private String message;
    private final ArrayList<Album> topAlbums = new ArrayList<>();
    private final int maxAlbums = 5;

    public void returnGetTop5Albums(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        // Get all albums from the library
        ArrayList<Album> allAlbums = new ArrayList<>(library.getAlbums());

        // Calculate the total likes for each album
        List<AlbumLikes> albumLikesList = allAlbums.stream()
                .map(album -> new AlbumLikes(album, calculateTotalLikes(album)))
                .collect(Collectors.toList());

        // Sort the albumLikesList in descending order by total likes and get the top 5 albums
        List<Album> top5Albums = albumLikesList.stream()
                .sorted(Comparator.comparingInt(AlbumLikes::getTotalLikes).reversed())
                .limit(maxAlbums)
                .map(AlbumLikes::getAlbum)
                .collect(Collectors.toList());

        topAlbums.addAll(top5Albums);
    }

    // Helper method to calculate the total likes for an album
    private int calculateTotalLikes(Album album) {
        return album.getSongs().stream()
                .mapToInt(song -> song.getUserLikesMap().size())
                .sum();
    }

    // Helper class to store album and total likes
    private static class AlbumLikes {
        private final Album album;
        private final int totalLikes;

        public AlbumLikes(Album album, int totalLikes) {
            this.album = album;
            this.totalLikes = totalLikes;
        }

        public Album getAlbum() {
            return album;
        }

        public int getTotalLikes() {
            return totalLikes;
        }
    }


}
