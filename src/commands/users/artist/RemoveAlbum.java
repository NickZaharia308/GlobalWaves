package commands.users.artist;

import commands.Command;
import commands.users.Shuffle;
import commands.users.Status;
import lombok.Getter;
import main.Library;
import userEntities.Artist;
import userEntities.Users;
import userEntities.audio.Album;
import userEntities.audio.Playlists;
import userEntities.audio.Songs;

import java.util.ArrayList;
import java.util.Iterator;

@Getter
public class RemoveAlbum extends Command {
    private String message;
    public void returnRemoveAlbum(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());
        super.setName(command.getName());

        Users user = new Users();
        user = user.getUser(library.getUsers(), this.getUsername());

        if (user == null) {
            setMessage("The username " + this.getUsername() + " doesn't exist.");
            return;
        }

        if (user.getUserType() != Users.UserType.ARTIST) {
            setMessage(this.getUsername() + " is not an artist.");
            return;
        }

        Artist artist = (Artist) user;
        Album albumToDelete = null;
        for (Album album : artist.getAlbums()) {
            if (album.getName().equals(this.getName())) {
                albumToDelete = album;
                break;
            }
        }

        if (albumToDelete == null) {
            setMessage(this.getUsername() + " doesn't have an album with the given name.");
            return;
        }

        if (isAlbumLoaded(library, command, albumToDelete)) {
            setMessage(this.getUsername() + " can't delete this album.");
            return;
        }

        removeSongsFromEverywhere(library, command, albumToDelete);
        artist.getAlbums().remove(albumToDelete);
        setMessage(this.getUsername() +" deleted the album successfully.");
    }

    private boolean isAlbumLoaded(Library library, Command command, Album albumToDelete) {

        ArrayList<Users> allUsers = library.getUsers();

        for (Users user: allUsers) {
            Status status = new Status();
            command.setUsername(user.getUsername());
            status.returnStatus(command, library);

            // If the user has an album loaded and the album is the album we want to delete
            if (user.isSomethingLoaded() && user.getTrackType() == Users.Track.ALBUM
                && user.getMusicPlayer().getAlbum().getName().equals(albumToDelete.getName())) {
                return true;
            }

            // Iterate through songs of the album and check if one song is loaded
            if (user.isSomethingLoaded() && (user.getTrackType() == Users.Track.SONG
                    || user.getTrackType() == Users.Track.PLAYLIST)) {
                for (Songs song : albumToDelete.getSongs()) {
                    // If the name of the album song is the name of the song that user has on track
                    if (song.getName().equals(user.getMusicPlayer().getSong().getName())) {
                        return true;
                    }
                }
            }

        }

        return false;
    }

    private void removeSongsFromEverywhere (Library library, Command command, Album albumToDelete) {
        // Delete all album songs from playlists
        ArrayList<Playlists> allPlaylists = library.getPlaylists();
        // Search in all playlists
        for (Playlists playlist : allPlaylists) {
            // Create an iterator for the playlist songs
            Iterator<Songs> playlistSongIterator = playlist.getSongs().iterator();

            // In all songs of the playlist
            while (playlistSongIterator.hasNext()) {
                Songs playlistSong = playlistSongIterator.next();

                // Compare to all songs of the album
                for (Songs songToDelete : albumToDelete.getSongs()) {
                    if (playlistSong.getName().equals(songToDelete.getName())) {
                        playlistSongIterator.remove();
                    }
                }
            }
        }

        // Delete all album songs from users' currentPlaylists
        ArrayList<Users> allUsers = library.getUsers();

        for (Users user : allUsers) {
            if (user.isSomethingLoaded() && user.getTrackType() == Users.Track.PLAYLIST) {
                // Current Playlist
                removeSongsFromPlaylist(user.getMusicPlayer().getPlaylist(), albumToDelete.getSongs());

                // Shuffled Playlist
                removeSongsFromPlaylist(user.getMusicPlayer().getPlaylistsShuffled(), albumToDelete.getSongs());
            }

            removeSongFromLikedSongs(user.getLikedSongs(), albumToDelete.getSongs());
        }

    }

    // Helper method to remove songs from a playlist
    private void removeSongsFromPlaylist(Playlists playlist, ArrayList<Songs> songsToRemove) {
        Iterator<Songs> playlistSongIterator = playlist.getSongs().iterator();

        while (playlistSongIterator.hasNext()) {
            Songs playlistSong = playlistSongIterator.next();

            for (Songs song : songsToRemove) {
                // If the name of the album song is the name of the song that user has in the playlist
                if (song.getName().equals(playlistSong.getName())) {
                    playlistSongIterator.remove();
                }
            }
        }
    }

    // Helper method to remove songs from the liked songs of a user
    private void removeSongFromLikedSongs(ArrayList<Songs> likedSongs, ArrayList<Songs> songsToRemove) {
        Iterator<Songs> likedSongsIterator = likedSongs.iterator();

        while (likedSongsIterator.hasNext()) {
            Songs likedSong = likedSongsIterator.next();
            for (Songs songToRemove : songsToRemove) {
                if (songToRemove.getName().equals(likedSong.getName())) {
                    likedSongsIterator.remove();
                }
            }
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
