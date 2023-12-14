package user.entities.specialEntities;

import lombok.Getter;
import main.Library;
import user.entities.Artist;
import user.entities.Host;
import user.entities.Users;
import user.entities.audio.files.Playlists;
import user.entities.audio.files.Songs;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * The PageMenu class represents a menu for different user pages, including the homepage,
 * liked content page, artist page, and host page.
 */
@Getter
public class PageMenu {

    // Type of page
    private Page currentPage = Page.HOMEPAGE;
    private final int maxShowed = 5;
    // The owner (Artist or Host) of the page that will be displayed
    private String pageOwnerName;

    /**
     * Enum representing different types of pages.
     */
    public enum Page {
        HOMEPAGE, LIKEDCONTENTPAGE, ARTISTPAGE, HOSTPAGE
    }

    /**
     * Sets the page based on the user's current page and the provided parameters.
     *
     * @param user   The user for whom to set the page.
     * @param library The library containing user information.
     * @param name   The name associated with the page.
     */
    public void setPage(final Users user, final Library library, final String name) {
        Page usersCurrentPage = user.getPageMenu().currentPage;
        if (usersCurrentPage == Page.HOMEPAGE || user.getUsername().equals(name)) {
            createHomepage(user);
        } else if (usersCurrentPage == Page.LIKEDCONTENTPAGE) {
            createLikedContentPage(user);
        } else if (usersCurrentPage == Page.ARTISTPAGE) {
            setArtistPage(user, library, name);
        } else if (usersCurrentPage == Page.HOSTPAGE) {
            setHostPage(user, library, name);
        }
    }

    private void createHomepage(final Users user) {
        ArrayList<Songs> likedSongs = sortLikedSongs(user.getLikedSongs());
        ArrayList<Playlists> followedPlaylists = user.getFollowedPlaylists();

        final int maxSongs = Math.min(likedSongs.size(), maxShowed);
        final int maxPlaylists = Math.min(followedPlaylists.size(), maxShowed);

        StringBuilder messageBuilder = new StringBuilder("Liked songs:\n\t[");
        for (int i = 0; i < maxSongs; i++) {
            messageBuilder.append(likedSongs.get(i).getName());

            // Add a comma if it's not the last element
            if (i < maxSongs - 1) {
                messageBuilder.append(", ");
            }
        }
        messageBuilder.append("]\n\nFollowed playlists:\n\t[");
        for (int i = 0; i < maxPlaylists; i++) {
            messageBuilder.append(followedPlaylists.get(i).getName());

            // Add a comma if it's not the last element
            if (i < maxPlaylists - 1) {
                messageBuilder.append(", ");
            }
        }
        messageBuilder.append("]");
        user.setCurrentPage(messageBuilder.toString());
    }

    private void createLikedContentPage(final Users user) {
        ArrayList<Songs> likedSongs = user.getLikedSongs();
        ArrayList<Playlists> followedPlaylists = user.getFollowedPlaylists();

        StringBuilder messageBuilder = new StringBuilder("Liked songs:\n\t[");
        for (int i = 0; i < likedSongs.size(); i++) {
            messageBuilder.append(likedSongs.get(i).getName() + " - "
                    + likedSongs.get(i).getArtist());

            // Add a comma if it's not the last element
            if (i < likedSongs.size() - 1) {
                messageBuilder.append(", ");
            }
        }
        messageBuilder.append("]\n\nFollowed playlists:\n\t[");
        for (int i = 0; i < followedPlaylists.size(); i++) {
            messageBuilder.append(followedPlaylists.get(i).getName() + " - "
                    + followedPlaylists.get(i).getOwner());

            // Add a comma if it's not the last element
            if (i < followedPlaylists.size() - 1) {
                messageBuilder.append(", ");
            }
        }
        messageBuilder.append("]");
        user.setCurrentPage(messageBuilder.toString());
    }

    /**
     * Sets the artist page for the user based on the provided artist name.
     *
     * @param user       The user for whom to set the page.
     * @param library    The library containing user information.
     * @param artistName The name of the artist for the page.
     */
    public void setArtistPage(final Users user, final Library library, final String artistName) {
        Artist artist = null;
        for (Users searchedArtist : library.getUsers()) {
            // If the user has the artist's name and the user is an artist
            if (searchedArtist.getUsername().equals(artistName)
                && searchedArtist.getUserType() == Users.UserType.ARTIST) {
                artist = (Artist) searchedArtist;
                break;
            }
        }

        if (artist == null) {
            return;
        }

        user.setCurrentPage(artist.toString());
    }

    /**
     * Sets the host page for the user based on the provided host name.
     *
     * @param user     The user for whom to set the page.
     * @param library  The library containing user information.
     * @param hostName The name of the host for the page.
     */
    public void setHostPage(final Users user, final Library library, final String hostName) {
        Host host = null;
        for (Users searchedHost : library.getUsers()) {
            // If the user has the host's name and the user is a host
            if (searchedHost.getUsername().equals(hostName)
                && searchedHost.getUserType() == Users.UserType.HOST) {
                host = (Host) searchedHost;
                break;
            }
        }

        if (host == null) {
            return;
        }

        user.setCurrentPage(host.toString());
    }

    /**
     * Sorts liked songs based on the number of likes in descending order.
     *
     * @param likedSongs The list of liked songs to be sorted.
     * @return The sorted list of liked songs.
     */
    private ArrayList<Songs> sortLikedSongs(final ArrayList<Songs> likedSongs) {
        ArrayList<Songs> sortedSongs = new ArrayList<>(likedSongs);
        sortedSongs.sort(Comparator.comparingLong(Songs::getNumberOfLikes).reversed());
        return sortedSongs;
    }

    /**
     * Sets the current page type.
     *
     * @param currentPage The type of page to be set.
     */
    public void setCurrentPage(final Page currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * Sets the page owner's name.
     *
     * @param pageOwnerName The name of the page owner to be set.
     */
    public void setPageOwnerName(final String pageOwnerName) {
        this.pageOwnerName = pageOwnerName;
    }
}
