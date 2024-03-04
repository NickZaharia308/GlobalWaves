package user.entities.audio.files;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The Songs class represents a song in the system.
 */
@Getter
@Setter
public class Songs {
    // "Default" fields
    private String name;
    private int duration;
    private String album;
    private ArrayList<String> tags;
    private String lyrics, genre;
    private int releaseYear;
    private String artist;

    // Added fields
    private Map<String, Boolean> userLikesMap = new HashMap<>();
    private int adPrice = 0;

    /**
     * Constructs a new Songs object with the specified attributes.
     *
     * @param name        The name of the song.
     * @param duration    The duration of the song.
     * @param album       The album to which the song belongs.
     * @param tags        The list of tags associated with the song.
     * @param lyrics      The lyrics of the song.
     * @param genre       The genre of the song.
     * @param releaseYear The release year of the song.
     * @param artist      The artist who performed the song.
     */
    public Songs(final String name, final int duration, final String album,
                 final ArrayList<String> tags, final String lyrics, final String genre,
                 final int releaseYear, final String artist) {
        this.name = name;
        this.duration = duration;
        this.album = album;
        this.tags = tags;
        this.lyrics = lyrics;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.artist = artist;
    }

    /**
     * Copy constructor for creating a new Songs object from an existing one.
     *
     * @param original The original Songs object to be copied.
     */
    public Songs(final Songs original) {
        this.name = original.name;
        this.duration = original.duration;
        this.album = original.album;

        // Copying ArrayList using a copy constructor or a constructor that accepts a Collection
        this.tags = new ArrayList<>(original.tags);

        this.lyrics = original.lyrics;
        this.genre = original.genre;
        this.releaseYear = original.releaseYear;
        this.artist = original.artist;
        this.userLikesMap = original.userLikesMap;
    }

    /**
     * Gets the number of likes for the song.
     *
     * @return The number of likes for the song.
     */
    public long getNumberOfLikes() {
        return userLikesMap.values().stream().filter(Boolean::booleanValue).count();
    }

    /**
     * Sets the price for advertising the song.
     * @param adPrice The price for advertising the song.
     */
    public void setAdPrice(final int adPrice) {
        this.adPrice = adPrice;
    }
}
