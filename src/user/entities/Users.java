package user.entities;

import commands.page.PageObserver;
import commands.users.notifications.NotificationObserver;
import commands.users.notifications.NotificationSubject;
import lombok.Getter;
import lombok.Setter;
import main.Library;
import user.entities.audio.MusicPlayer;
import user.entities.audio.files.Album;
import user.entities.specialEntities.PageMenu;
import user.entities.audio.files.Playlists;
import user.entities.audio.files.Songs;

import java.util.*;

/**
 * The Users class represents a user in the system.
 */
@Setter
@Getter
public class Users implements PageObserver, NotificationObserver {

    // "Default" fields
    protected String username;
    protected int age;
    protected String city;

    // Added fields
    private MusicPlayer musicPlayer = null;
    private LinkedList<String> searchResults = null;
    private int noOfSearchResults = -1;
    private boolean isSomethingSelected = false;
    private boolean isSomethingLoaded = false;
    private Track trackType = null;
    private ArrayList<Songs> likedSongs = new ArrayList<>();
    private int noOfPlaylists = 0;

    // Added fields etapa 2
    private boolean isOnline = true;
    private UserType userType = UserType.NORMAL;

    private ArrayList<Playlists> followedPlaylists = new ArrayList<>();
    private PageMenu pageMenu = new PageMenu();

    // The string that will be displayed when the command is "PrintCurrentPage"
    private String currentPage;
    private boolean pageSearched = false;

    // Added fields etapa 3
    //Wrapped
    private Map<String, Integer> topArtists = new HashMap<>();
    private Map<String, Integer> topGenres = new HashMap<>();
    private Map<String, Integer> topSongs = new HashMap<>();
    private Map<String, Integer> topAlbums = new HashMap<>();
    private Map<String, Integer> topEpisodes = new HashMap<>();
    private ArrayList<Album> albumResults = new ArrayList<>();
    // Monetization
    private boolean premium;
    private Map<Songs, Integer> songsFromArtists = new HashMap<>();
    private String lastSong;
    // Subscribe and Notification
    private Queue<String> notifications = new LinkedList<>();
    private ArrayList<String> boughtMerchandise = new ArrayList<>();


    /**
     * Enumeration representing different types of tracks.
     */
    public enum Track {
        SONG, PLAYLIST, PODCAST, ALBUM
    }

    public enum UserType {
        NORMAL, ARTIST, HOST
    }

    /**
     * Constructs a new Users object with the specified username, age, and city.
     *
     * @param username The username of the user.
     * @param age      The age of the user.
     * @param city     The city where the user resides.
     */
    public Users(final String username, final int age, final String city) {
        this.username = username;
        this.age = age;
        this.city = city;

        // Set the current page to home
        this.getPageMenu().setPageOwnerName(username);
    }

    /**
     * Default constructor for the Users class.
     * Creates an instance of Users with default values or initializes fields to null.
     */
    public Users() { }

    /**
     * Gets a user with the specified name from the list of users.
     *
     * @param users The list of users.
     * @param name  The name of the user to retrieve.
     * @return The user with the specified name, or null if not found.
     */
    public Users getUser(final ArrayList<Users> users, final String name) {
        for (Users user : users) {
            if (user.getUsername().equals(name)) {
                return user;
            }
        }
        return null;
    }


    /**
     * Updates the user's current page (Home, Liked Content, Host or Artist page).
     *
     * @param library The Library instance representing the current state of the system.
     */
    @Override
    public final void update(final Library library) {
        this.getPageMenu().setPage(this, library, this.getPageMenu().getPageOwnerName());
    }

    @Override
    public final void update(final NotificationSubject notificationSubject) {
        Artist artist = (Artist) notificationSubject;
        notifications.add(artist.getNotificationName());
        notifications.add(artist.getNotificationDescription());
    }
}
