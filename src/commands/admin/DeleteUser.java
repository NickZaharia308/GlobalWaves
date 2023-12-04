package commands.admin;

import commands.Command;
import commands.users.Status;
import lombok.Getter;
import main.Library;
import userEntities.Artist;
import userEntities.Users;
import userEntities.audio.Album;
import userEntities.audio.Songs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

@Getter
public class DeleteUser extends Command {
    private String message;

    public void returnDeleteUser(Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setTimestamp(command.getTimestamp());
        super.setUsername(command.getUsername());

        ArrayList<Users> allUsers= library.getUsers();

        // If the user doesn't exist
        Users user = new Users();
        user = user.getUser(allUsers, this.getUsername());
        if (user == null) {
            setMessage("The username " + this.getUsername() + " doesn't exist.");
            return;
        }

        if (user.getUserType() == Users.UserType.ARTIST && deleteArtist(command, library)) {
            return;
        }

        if (user.getUserType() == Users.UserType.ARTIST) {
            deleteEverythingFromArtist(user, library);
        }
        setMessage(this.getUsername() + " was successfully deleted.");
    }

    private boolean deleteArtist (Command command, final Library library) {
        ArrayList<Users> allUsers= library.getUsers();

        Users user = new Users();
        user = user.getUser(allUsers, this.getUsername());

        Artist artist = (Artist) user;

        // Search through all users
        for (Users currentUser : allUsers) {
            Status status = new Status();
            command.setUsername(currentUser.getUsername());
            status.returnStatus(command, library);


            // If the user has an album loaded
            if (currentUser.getTrackType() == Users.Track.ALBUM) {

                // If the owner of the album is the artist himself
                if (currentUser.getMusicPlayer().getAlbum().getOwner().equals(artist.getUsername())) {

                    // If the user has something from the album loaded
                    if (currentUser.isSomethingLoaded()) {
                        setMessage(artist.getUsername() + " can't be deleted.");
                        return true;
                    }
                }
            }

            // If the user has a song from the artist's album
            if (currentUser.getTrackType() == Users.Track.SONG) {

                // If the owner of the song is the artist himself
                if (currentUser.getMusicPlayer().getSong().getArtist().equals(artist.getUsername())) {

                    // If the user has something from the album loaded
                    if (currentUser.isSomethingLoaded()) {
                        setMessage(artist.getUsername() + " can't be deleted.");
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private void deleteEverythingFromArtist (Users user, Library library) {
        Artist artist = (Artist) user;

        // Delete the artist's songs from the library
        ArrayList<Songs> allSongs = library.getSongs();
        Iterator<Songs> songIterator = allSongs.iterator();
        while (songIterator.hasNext()) {
            Songs song = songIterator.next();

            // Remove the song
            if (song.getArtist().equals(artist.getUsername())) {

                // Iterate through the users that liked the song and remove the song
                for (Map.Entry<String, Boolean> entry : song.getUserLikesMap().entrySet()) {
                    String userName = entry.getKey();
                    Users currentUser = new Users();
                    currentUser = currentUser.getUser(library.getUsers(), userName);
                    currentUser.getLikedSongs().remove(song);
                }

                songIterator.remove();
            }
        }

        // Delete the artist's albums from the library
        ArrayList<Album> allAlbums = library.getAlbums();
        Iterator<Album> albumIterator = allAlbums.iterator();
        while (albumIterator.hasNext()) {
            Album album = albumIterator.next();
            if (album.getOwner().equals(artist.getUsername())) {
                albumIterator.remove();
            }
        }

        // Remove the artist
        library.getUsers().remove(artist);
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
