package commands.admin;

import commands.Command;
import commands.users.Status;
import lombok.Getter;
import main.Library;
import userEntities.Artist;
import userEntities.Host;
import userEntities.Users;
import userEntities.audio.Album;
import userEntities.audio.Playlists;
import userEntities.audio.Podcasts;
import userEntities.audio.Songs;
import userEntities.specialEntities.PageMenu;

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

        if (user.getUserType() == Users.UserType.NORMAL && deleteUser(command, library)) {
            return;
        }

        if (user.getUserType() == Users.UserType.HOST && deleteHost(command, library)) {
            return;
        }

        if (user.getUserType() == Users.UserType.ARTIST) {
            deleteEverythingFromArtist(user, library);
        }

        if (user.getUserType() == Users.UserType.NORMAL) {
            deleteEverythingFromUser(user, library);
            deleteUserFromEverywhere(user, library);
        }

        if (user.getUserType() == Users.UserType.HOST) {
            deleteEverythingFromHost(user, library);
        }

        // Remove the user
        library.getUsers().remove(user);
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


            // If the user has a playlist with a song from the artist's album
            if (currentUser.getTrackType() == Users.Track.PLAYLIST && currentUser.isSomethingLoaded()) {

                // If one of the songs owner from the current playlist is the artist
                boolean hasSongFromArtistAlbum = currentUser.getMusicPlayer().getPlaylist().getSongs().stream()
                        .anyMatch(song -> song.getArtist().equals(artist.getUsername()));

                // If the playlist has a song from the artist's album
                if (hasSongFromArtistAlbum) {
                    setMessage(artist.getUsername() + " can't be deleted.");
                    return true;
                }
            }

            // If a user has the artist's page selected
            if (currentUser.getPageMenu().getCurrentPage() == PageMenu.Page.ARTISTPAGE &&
                    currentUser.getPageMenu().getPageOwnerName().equals(artist.getUsername())) {
                setMessage(artist.getUsername() + " can't be deleted.");
                return true;
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

    }

    private boolean deleteUser(Command command, final Library library) {
        ArrayList<Users> allUsers= library.getUsers();

        Users user = new Users();
        user = user.getUser(allUsers, this.getUsername());

        // Search through all users
        for (Users currentUser : allUsers) {
            Status status = new Status();
            command.setUsername(currentUser.getUsername());
            status.returnStatus(command, library);

            // If the user has a playlist loaded
            if (currentUser.isSomethingLoaded() &&
                currentUser.getTrackType() == Users.Track.PLAYLIST) {

                // If the owner of the playlist is the user himself
                if (currentUser.getMusicPlayer().getPlaylist().getOwner().equals(user.getUsername())) {
                    setMessage(user.getUsername() + " can't be deleted.");
                    return true;
                }
            }
        }

        return false;
    }

    private void deleteEverythingFromUser(Users user, Library library) {

        // Delete the user's playlists from the library
        ArrayList<Playlists> allPlaylists = library.getPlaylists();
        Iterator<Playlists> playlistIterator = allPlaylists.iterator();
        while (playlistIterator.hasNext()) {
            Playlists playlist = playlistIterator.next();

            // Remove the playlist
            if (playlist.getOwner().equals(user.getUsername())) {

                // Iterate through the users that follow the playlist and remove the playlist
                for (Map.Entry<String, Boolean> entry : playlist.getUserFollowMap().entrySet()) {
                    String userName = entry.getKey();
                    Users currentUser = new Users();
                    currentUser = currentUser.getUser(library.getUsers(), userName);
                    currentUser.getFollowedPlaylists().remove(playlist);
                }

                playlistIterator.remove();
            }
        }
    }

    private void deleteUserFromEverywhere(Users user, Library library) {

        // Lambda expressions that removes the user's like from songs and its follow from playlists
        library.getSongs().forEach(song -> song.getUserLikesMap().remove(user.getUsername()));

        library.getPlaylists().forEach(playlist -> playlist.getUserFollowMap().
                                                    remove((user.getUsername())));
        library.getPlaylists().forEach(playlist -> playlist.setFollowers
                                                    (playlist.getFollowers() - 1));
    }


    private boolean deleteHost (Command command, final Library library) {
        ArrayList<Users> allUsers= library.getUsers();

        Users user = new Users();
        user = user.getUser(allUsers, this.getUsername());

        Host host = (Host) user;

        // Search through all users
        for (Users currentUser : allUsers) {
            Status status = new Status();
            command.setUsername(currentUser.getUsername());
            status.returnStatus(command, library);


            // If the user has a podcast started
            if (currentUser.getMusicPlayer() != null &&
                currentUser.getMusicPlayer().getPodcast() != null) {

                // If the owner of the podcast is the host himself
                if (currentUser.getMusicPlayer().getPodcast().getOwner().equals(host.getUsername())) {
                    setMessage(host.getUsername() + " can't be deleted.");
                    return true;

                }
            }

            // If a user has the host's page selected
            if (currentUser.getPageMenu().getCurrentPage() == PageMenu.Page.HOSTPAGE &&
                currentUser.getPageMenu().getPageOwnerName().equals(host.getUsername())) {
                setMessage(host.getUsername() + " can't be deleted.");
                return true;
            }
        }
        return false;
    }

    private void deleteEverythingFromHost (Users user, Library library) {
        Host host = (Host) user;

        // Delete the host's albums from the library

        ArrayList<Podcasts> allPodcasts = library.getPodcasts();
        Iterator<Podcasts> podcastIterator = allPodcasts.iterator();
        while (podcastIterator.hasNext()) {
            Podcasts podcast = podcastIterator.next();
            if (podcast.getOwner().equals(host.getUsername())) {
                podcastIterator.remove();
            }
        }

    }

    public void setMessage(String message) {
        this.message = message;
    }
}
