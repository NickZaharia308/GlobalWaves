package userEntities;

import lombok.Getter;
import userEntities.audio.Playlists;
import userEntities.audio.Songs;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * The Users class represents a user in the system.
 */
public class Users {

    // "Default" fields
    @Getter
    protected String username;
    @Getter
    protected int age;
    @Getter
    protected String city;

    // Added fields
    @Getter
    private MusicPlayer musicPlayer = null;
    @Getter
    private LinkedList<String> searchResults = null;
    @Getter
    private int noOfSearchResults = -1;
    @Getter
    private boolean isSomethingSelected = false;
    @Getter
    private boolean isSomethingLoaded = false;
    @Getter
    private Track trackType = null;
    @Getter
    private ArrayList<Songs> likedSongs = new ArrayList<>();
    @Getter
    private int noOfPlaylists = 0;

    // Added fields etapa 2
    @Getter
    private boolean isOnline = true;
    @Getter
    private UserType userType = UserType.NORMAL;

    @Getter
    private ArrayList<Playlists> followedPlaylists = new ArrayList<>();

    /**
     * Default constructor for the Users class.
     * Creates an instance of Users with default values or initializes fields to null.
     */
    public Users() {
        // Implementation details, if necessary
    }

    /**
     * Enumeration representing different types of tracks.
     */
    public enum Track {
        SONG, PLAYLIST, PODCAST
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
    }

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
     * Sets the username of the user.
     *
     * @param username The new username.
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Sets the age of the user.
     *
     * @param age The new age.
     */
    public void setAge(final int age) {
        this.age = age;
    }

    /**
     * Sets the city of the user.
     *
     * @param city The new city.
     */
    public void setCity(final String city) {
        this.city = city;
    }

    /**
     * Sets the music player for the user.
     *
     * @param musicPlayer The music player.
     */
    public void setMusicPlayer(final MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    /**
     * Sets the search results for the user.
     *
     * @param searchResults The search results.
     */
    public void setSearchResults(final LinkedList<String> searchResults) {
        this.searchResults = searchResults;
    }

    /**
     * Sets the number of search results for the user.
     *
     * @param noOfSearchResults The number of search results.
     */
    public void setNoOfSearchResults(final int noOfSearchResults) {
        this.noOfSearchResults = noOfSearchResults;
    }

    /**
     * Checks if something is selected for the user.
     *
     * @return True if something is selected, false otherwise.
     */
    public boolean isSomethingSelected() {
        return isSomethingSelected;
    }

    /**
     * Sets whether something is selected for the user.
     *
     * @param somethingSelected True if something is selected, false otherwise.
     */
    public void setSomethingSelected(final boolean somethingSelected) {
        isSomethingSelected = somethingSelected;
    }

    /**
     * Checks if something is loaded for the user.
     *
     * @return True if something is loaded, false otherwise.
     */
    public boolean isSomethingLoaded() {
        return isSomethingLoaded;
    }

    /**
     * Sets whether something is loaded for the user.
     *
     * @param somethingLoaded True if something is loaded, false otherwise.
     */
    public void setSomethingLoaded(final boolean somethingLoaded) {
        isSomethingLoaded = somethingLoaded;
    }

    /**
     * Sets the track type for the user.
     *
     * @param trackType The track type.
     */
    public void setTrackType(final Track trackType) {
        this.trackType = trackType;
    }

    /**
     * Sets the list of liked songs for the user.
     *
     * @param likedSongs The list of liked songs.
     */
    public void setLikedSongs(final ArrayList<Songs> likedSongs) {
        this.likedSongs = likedSongs;
    }

    /**
     * Sets the number of playlists for the user.
     *
     * @param noOfPlaylists The number of playlists.
     */
    public void setNoOfPlaylists(final int noOfPlaylists) {
        this.noOfPlaylists = noOfPlaylists;
    }


    public void setOnline(boolean online) {
        isOnline = online;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public void setFollowedPlaylists(ArrayList<Playlists> followedPlaylists) {
        this.followedPlaylists = followedPlaylists;
    }
}
