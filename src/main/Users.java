package main;

import java.util.ArrayList;
import java.util.LinkedList;

public class Users {

    // "Default" fields
    private String username;
    private int age;
    private String city;

    // Added fields
    MusicPlayer musicPlayer = null;
    private LinkedList<String> searchResults = null;
    private int noOfSearchResults = -1;
    private boolean isSomethingSelected = false;
    private boolean isSomethingLoaded = false;
    private Track trackType = null;
    private ArrayList<Songs> likedSongs = new ArrayList<>();
    private int noOfPlaylists = 0;

    enum Track {
        SONG, PLAYLIST, PODCAST
    }


    public Users(String username, int age, String city) {
        this.username = username;
        this.age = age;
        this.city = city;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public void setMusicPlayer(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    public LinkedList<String> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(LinkedList<String> searchResults) {
        this.searchResults = searchResults;
    }

    public int getNoOfSearchResults() {
        return noOfSearchResults;
    }

    public void setNoOfSearchResults(int noOfSearchResults) {
        this.noOfSearchResults = noOfSearchResults;
    }

    public boolean isSomethingSelected() {
        return isSomethingSelected;
    }

    public void setSomethingSelected(boolean somethingSelected) {
        isSomethingSelected = somethingSelected;
    }

    public boolean isSomethingLoaded() {
        return isSomethingLoaded;
    }

    public void setSomethingLoaded(boolean somethingLoaded) {
        isSomethingLoaded = somethingLoaded;
    }

    public Track getTrackType() {
        return trackType;
    }

    public void setTrackType(Track trackType) {
        this.trackType = trackType;
    }

    public ArrayList<Songs> getLikedSongs() {
        return likedSongs;
    }

    public void setLikedSongs(ArrayList<Songs> likedSongs) {
        this.likedSongs = likedSongs;
    }

    public int getNoOfPlaylists() {
        return noOfPlaylists;
    }

    public void setNoOfPlaylists(int noOfPlaylists) {
        this.noOfPlaylists = noOfPlaylists;
    }
}
