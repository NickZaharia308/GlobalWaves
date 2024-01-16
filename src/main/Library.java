package main;

import commands.page.observer.PageObserver;
import lombok.Getter;
import user.entities.Users;
import user.entities.audio.files.Album;
import user.entities.audio.files.Playlists;
import user.entities.audio.files.Podcasts;
import user.entities.audio.files.Songs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The Library class represents a library of users, songs, albums, podcasts, and playlists.
 * It follows the Singleton pattern to ensure only one instance exists in the application.
 */
@Getter
public final class Library {
    private static Library instance;

    private ArrayList<Users> users;
    private ArrayList<Songs> songs;
    private ArrayList<Podcasts> podcasts;
    private ArrayList<Playlists> playlists;
    private ArrayList<Album> albums;
    private Map<String, ArrayList<PageObserver>> observersMap;

    /**
     * Private constructor to prevent external instantiation.
     * Initializes the lists for users, songs, podcasts, playlists, albums and the observersMap.
     */
    private Library() {
        this.users = new ArrayList<>();
        this.songs = new ArrayList<>();
        this.podcasts = new ArrayList<>();
        this.playlists = new ArrayList<>();
        this.albums = new ArrayList<>();
        this.observersMap = new HashMap<>();
    }

    /**
     * Returns the singleton instance of the Library class.
     * If the instance does not exist, it creates a new one.
     *
     * @return The singleton instance of the Library class.
     */
    public static Library getInstance() {
        if (instance == null) {
            instance = new Library();
        }
        return instance;
    }

    /**
     * Sets the list of users in the library.
     *
     * @param users The list of users to set.
     */
    public void setUsers(final ArrayList<Users> users) {
        this.users = users;
    }

    /**
     * Sets the list of songs in the library.
     *
     * @param songs The list of songs to set.
     */
    public void setSongs(final ArrayList<Songs> songs) {
        this.songs = songs;
    }

    /**
     * Sets the list of podcasts in the library.
     *
     * @param podcasts The list of podcasts to set.
     */
    public void setPodcasts(final ArrayList<Podcasts> podcasts) {
        this.podcasts = podcasts;
    }

    /**
     * Sets the list of playlists in the library.
     *
     * @param playlists The list of playlists to set.
     */
    public void setPlaylists(final ArrayList<Playlists> playlists) {
        this.playlists = playlists;
    }

    /**
     * Sets the list of albums in the library.
     *
     * @param albums The list of albums to set.
     */
    public void setAlbums(final ArrayList<Album> albums) {
        this.albums = albums;
    }

    /**
     * Sets the observer map in the library.
     *
     * @param observersMap The observer map to set.
     */
    public void setObserversMap(final Map<String, ArrayList<PageObserver>> observersMap) {
        this.observersMap = observersMap;
    }

    /**
     * Resets the library by creating new instances of lists and maps.
     */
    public void reset() {
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        observersMap = new HashMap<>();
    }
}
