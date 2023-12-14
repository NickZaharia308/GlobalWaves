package commands.admin;

import commands.Command;
import commands.users.Status;
import lombok.Getter;
import main.Library;
import user.entities.Artist;
import user.entities.Host;
import user.entities.Users;
import user.entities.audio.files.Album;
import user.entities.audio.files.Playlists;
import user.entities.audio.files.Podcasts;
import user.entities.audio.files.Songs;
import user.entities.specialEntities.PageMenu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * The DeleteUser class represents a command to delete a user
 * and associated entities from the library.
 */
@Getter
public class DeleteUser extends Command {
    private String message;

    /**
     * Deletes the specified user and associated entities from the library.
     *
     * @param command The command containing information about the requested operation.
     * @param library The main library containing user and entity data.
     */
    public void returnDeleteUser(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setTimestamp(command.getTimestamp());
        super.setUsername(command.getUsername());

        ArrayList<Users> allUsers = library.getUsers();

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
        } else if (user.getUserType() == Users.UserType.NORMAL) {
            deleteEverythingFromUser(user, library);
            deleteUserFromEverywhere(user, library);
        } else if (user.getUserType() == Users.UserType.HOST) {
            deleteEverythingFromHost(user, library);
        }

        // Remove the user
        library.getUsers().remove(user);
        setMessage(this.getUsername() + " was successfully deleted.");
    }

    /**
     * Checks if the specified artist can be safely deleted.
     *
     * @param command The command containing information about the requested operation.
     * @param library The main library containing user and entity data.
     * @return True if the artist can't be deleted, false otherwise.
     */
    private boolean deleteArtist(final Command command, final Library library) {
        ArrayList<Users> allUsers = library.getUsers();

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
                if (currentUser.getMusicPlayer().getAlbum().getOwner()
                    .equals(artist.getUsername())) {

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
                if (currentUser.getMusicPlayer().getSong().getArtist()
                    .equals(artist.getUsername())) {

                    // If the user has something from the album loaded
                    if (currentUser.isSomethingLoaded()) {
                        setMessage(artist.getUsername() + " can't be deleted.");
                        return true;
                    }
                }
            }

            // If the user has a playlist with a song from the artist's album
            if (currentUser.getTrackType() == Users.Track.PLAYLIST
                && currentUser.isSomethingLoaded()) {

                // If one of the songs owner from the current playlist is the artist
                boolean hasSongFromArtistAlbum = currentUser.getMusicPlayer().getPlaylist()
                        .getSongs().stream()
                        .anyMatch(song -> song.getArtist().equals(artist.getUsername()));

                // If the playlist has a song from the artist's album
                if (hasSongFromArtistAlbum) {
                    setMessage(artist.getUsername() + " can't be deleted.");
                    return true;
                }
            }

            // If a user has the artist's page selected
            if (currentUser.getPageMenu().getCurrentPage() == PageMenu.Page.ARTISTPAGE
                && currentUser.getPageMenu().getPageOwnerName().equals(artist.getUsername())) {
                setMessage(artist.getUsername() + " can't be deleted.");
                return true;
            }
        }

        return false;
    }

    /**
     * Deletes all songs and albums associated with the specified artist from the library.
     *
     * @param user    The artist user to be deleted.
     * @param library The main library containing user and entity data.
     */
    private void deleteEverythingFromArtist(final Users user, final Library library) {
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

    /**
     * Deletes a user and related data if the user is of type normal.
     * Checks if the user is the owner of any loaded playlist to prevent deletion.
     *
     * @param command The command containing information about the requested operation.
     * @param library The main library containing user and entity data.
     * @return True if the user can't be deleted, false otherwise.
     */
    private boolean deleteUser(final Command command, final Library library) {
        ArrayList<Users> allUsers = library.getUsers();

        Users user = new Users();
        user = user.getUser(allUsers, this.getUsername());

        // Search through all users
        for (Users currentUser : allUsers) {
            Status status = new Status();
            command.setUsername(currentUser.getUsername());
            status.returnStatus(command, library);

            // If the user has a playlist loaded
            if (currentUser.isSomethingLoaded()
                && currentUser.getTrackType() == Users.Track.PLAYLIST) {

                // If the owner of the playlist is the user himself
                if (currentUser.getMusicPlayer().getPlaylist().getOwner()
                    .equals(user.getUsername())) {
                    setMessage(user.getUsername() + " can't be deleted.");
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Deletes all playlists associated with the specified user from the library.
     *
     * @param user    The user to be deleted.
     * @param library The main library containing user and entity data.
     */
    private void deleteEverythingFromUser(final Users user, final Library library) {

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

    /**
     * Removes a user's like from songs and its follow from playlists.
     *
     * @param user    The user to be deleted.
     * @param library The main library containing user and entity data.
     */
    private void deleteUserFromEverywhere(final Users user, final Library library) {

        // Lambda expressions that removes the user's like from songs and its follow from playlists
        library.getSongs().forEach(song -> song.getUserLikesMap().remove(user.getUsername()));

        library.getPlaylists().forEach(playlist ->
                playlist.getUserFollowMap().remove((user.getUsername())));

        library.getPlaylists().forEach(playlist ->
                playlist.setFollowers(playlist.getFollowers() - 1));
    }


    /**
     * Checks if the specified host can be safely deleted.
     *
     * @param command The command containing information about the requested operation.
     * @param library The main library containing user and entity data.
     * @return True if the host can't be deleted, false otherwise.
     */
    private boolean deleteHost(final Command command, final Library library) {
        ArrayList<Users> allUsers = library.getUsers();

        Users user = new Users();
        user = user.getUser(allUsers, this.getUsername());

        Host host = (Host) user;

        // Search through all users
        for (Users currentUser : allUsers) {
            Status status = new Status();
            command.setUsername(currentUser.getUsername());
            status.returnStatus(command, library);


            // If the user has a podcast started
            if (currentUser.getMusicPlayer() != null
                && currentUser.getMusicPlayer().getPodcast() != null) {

                // If the owner of the podcast is the host himself
                if (currentUser.getMusicPlayer().getPodcast().getOwner()
                    .equals(host.getUsername())) {
                    setMessage(host.getUsername() + " can't be deleted.");
                    return true;

                }
            }

            // If a user has the host's page selected
            if (currentUser.getPageMenu().getCurrentPage() == PageMenu.Page.HOSTPAGE
                && currentUser.getPageMenu().getPageOwnerName().equals(host.getUsername())) {
                setMessage(host.getUsername() + " can't be deleted.");
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes all podcasts associated with the specified host from the library.
     *
     * @param user    The host user to be deleted.
     * @param library The main library containing user and entity data.
     */
    private void deleteEverythingFromHost(final Users user, final Library library) {
        Host host = (Host) user;

        // Delete the host's podcasts from the library
        ArrayList<Podcasts> allPodcasts = library.getPodcasts();
        Iterator<Podcasts> podcastIterator = allPodcasts.iterator();
        while (podcastIterator.hasNext()) {
            Podcasts podcast = podcastIterator.next();
            if (podcast.getOwner().equals(host.getUsername())) {
                podcastIterator.remove();
            }
        }
    }

    /**
     * Sets the message related to the execution of the DeleteUser command.
     *
     * @param message The message to be set.
     */
    public void setMessage(final String message) {
        this.message = message;
    }
}
