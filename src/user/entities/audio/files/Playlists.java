package user.entities.audio.files;

import lombok.Getter;
import lombok.Setter;
import main.Library;
import user.entities.Users;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * The Playlists class represents a playlist in the system.
 */
@Getter
@Setter
public class Playlists {
    private String name;
    private ArrayList<Songs> songs = null;
    private String visibility;
    private int followers = 0;
    private String owner;
    private int id;
    @Getter
    private static int noOfPlaylists = 0;
    private Map<String, Boolean> userFollowMap = new HashMap<>();

    // The ID of the owner in his personal collection
    private int usersID;

    /**
     * Constructs a new Playlists object with the specified attributes.
     *
     * @param name    The name of the playlist.
     * @param owner   The owner of the playlist.
     * @param library The library containing user information.
     */
    public Playlists(final String name, final String owner, final Library library) {
        this.name = name;
        this.songs = new ArrayList<>();
        this.visibility = "public";
        this.followers = 0;
        this.owner = owner;
        noOfPlaylists++;
        this.id = noOfPlaylists;
        Users user = getUser(owner, library);

        int noOfPlaylist = user.getNoOfPlaylists();
        noOfPlaylist++;
        this.usersID = noOfPlaylist;
        user.setNoOfPlaylists(noOfPlaylist);
    }

    /**
     * Copy constructor for creating a new Playlists object from an existing one.
     * The songs in the playlist are shuffled based on the provided seed.
     *
     * @param original The original Playlists object to be copied.
     * @param seed     The seed used for shuffling songs.
     */
    public Playlists(final Playlists original, final long seed) {
        this.name = original.name;
        this.visibility = original.visibility;
        this.followers = original.followers;
        this.owner = original.owner;
        this.id = original.id;
        this.usersID = original.usersID;

        // Create a new ArrayList for the shuffled songs
        this.songs = new ArrayList<>(original.songs);
        shuffleSongs(seed);
    }

    /**
     * Helper method to shuffle songs with a given seed.
     *
     * @param seed The seed used for shuffling songs.
     */
    private void shuffleSongs(final long seed) {
        if (this.songs != null) {
            Random random = new Random(seed);
            Collections.shuffle(this.songs, random);
        }
    }

    /**
     * Helper function to find a user with a given username.
     *
     * @param username The username of the user to find.
     * @param library  The library containing user information.
     * @return The found Users object or null if not found.
     */
    private Users getUser(final String username, final Library library) {
        for (Users user : library.getUsers()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Sets the total number of playlists in the system.
     *
     * @param noOfPlaylists The new total number of playlists.
     */
    public static void setNoOfPlaylists(final int noOfPlaylists) {
        Playlists.noOfPlaylists = noOfPlaylists;
    }
}
