package user.entities.audio.files;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The Songs class represents a song in the system.
 */
@Getter
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
     * Sets the name of the song.
     *
     * @param name The new name of the song.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Sets the duration of the song.
     *
     * @param duration The new duration of the song.
     */
    public void setDuration(final int duration) {
        this.duration = duration;
    }

    /**
     * Sets the album to which the song belongs.
     *
     * @param album The new album of the song.
     */
    public void setAlbum(final String album) {
        this.album = album;
    }

    /**
     * Sets the list of tags associated with the song.
     *
     * @param tags The new list of tags for the song.
     */
    public void setTags(final ArrayList<String> tags) {
        this.tags = tags;
    }

    /**
     * Sets the lyrics of the song.
     *
     * @param lyrics The new lyrics of the song.
     */
    public void setLyrics(final String lyrics) {
        this.lyrics = lyrics;
    }

    /**
     * Sets the genre of the song.
     *
     * @param genre The new genre of the song.
     */
    public void setGenre(final String genre) {
        this.genre = genre;
    }

    /**
     * Sets the release year of the song.
     *
     * @param releaseYear The new release year of the song.
     */
    public void setReleaseYear(final int releaseYear) {
        this.releaseYear = releaseYear;
    }

    /**
     * Sets the artist who performed the song.
     *
     * @param artist The new artist of the song.
     */
    public void setArtist(final String artist) {
        this.artist = artist;
    }

    /**
     * Sets the user likes map for the song.
     *
     * @param userLikesMap The new user likes map for the song.
     */
    public void setUserLikesMap(final Map<String, Boolean> userLikesMap) {
        this.userLikesMap = userLikesMap;
    }

    /**
     * Gets the number of likes for the song.
     *
     * @return The number of likes for the song.
     */
    public long getNumberOfLikes() {
        return userLikesMap.values().stream().filter(Boolean::booleanValue).count();
    }
}
