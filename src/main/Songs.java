package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public Songs(String name, int duration, String album, ArrayList<String> tags, String lyrics, String genre,
                 int releaseYear, String artist) {
        this.name = name;
        this.duration = duration;
        this.album = album;
        this.tags = tags;
        this.lyrics = lyrics;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.artist = artist;
    }

    // Copy constructor
    public Songs(Songs original) {
        this.name = original.name;
        this.duration = original.duration;
        this.album = original.album;

        // Copying ArrayList using a copy constructor or a constructor that accepts a Collection
        this.tags = new ArrayList<>(original.tags);

        this.lyrics = original.lyrics;
        this.genre = original.genre;
        this.releaseYear = original.releaseYear;
        this.artist = original.artist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Map<String, Boolean> getUserLikesMap() {
        return userLikesMap;
    }

    public void setUserLikesMap(Map<String, Boolean> userLikesMap) {
        this.userLikesMap = userLikesMap;
    }
}
