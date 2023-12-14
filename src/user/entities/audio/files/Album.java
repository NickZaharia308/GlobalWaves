package user.entities.audio.files;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * The Album class represents a collection of songs released by an artist.
 */
@Getter
public class Album {
    private String name;
    private int releaseYear;
    private String description;
    private ArrayList<Songs> songs = null;
    private String owner;

    /**
     * Constructs an Album object with the specified name,
     * release year, description, songs, and owner.
     *
     * @param name         The name of the album.
     * @param releaseYear  The release year of the album.
     * @param description  The description of the album.
     * @param songs        The list of songs in the album.
     * @param username     The username of the owner (artist) of the album.
     */
    public Album(final String name, final int releaseYear, final String description,
                 final ArrayList<Songs> songs, final String username) {
        this.name = name;
        this.releaseYear = releaseYear;
        this.description = description;
        this.songs = songs;
        this.owner = username;
    }

    /**
     * Copy constructor for the Album class.
     * Creates a new Album object with the same attributes as the original album and
     * shuffles the songs with the given seed.
     *
     * @param original The original album to be copied.
     * @param seed     The seed used for shuffling songs.
     */
    public Album(final Album original, final long seed) {
        this.name = original.name;
        this.releaseYear = original.releaseYear;
        this.description = original.description;
        this.owner = original.owner;

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
     * Sets the name of the album.
     *
     * @param name The new name of the album.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Sets the release year of the album.
     *
     * @param releaseYear The new release year of the album.
     */
    public void setReleaseYear(final int releaseYear) {
        this.releaseYear = releaseYear;
    }

    /**
     * Sets the description of the album.
     *
     * @param description The new description of the album.
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Sets the list of songs in the album.
     *
     * @param songs The new list of songs in the album.
     */
    public void setSongs(final ArrayList<Songs> songs) {
        this.songs = songs;
    }

    /**
     * Sets the owner (artist) of the album.
     *
     * @param owner The new owner (artist) of the album.
     */
    public void setOwner(final String owner) {
        this.owner = owner;
    }
}
