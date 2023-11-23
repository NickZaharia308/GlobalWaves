package main;

import java.util.*;

public class Playlists {
    private String name;
    private ArrayList<Songs> songs = null;
    private String visibility;
    private int followers = 0;
    private String owner;
    private int ID;
    static int noOfPlaylists = 0;
    private Map<String, Boolean> userFollowMap = new HashMap<>();

    // The ID of a user's playlist
    private int usersID;

    public Playlists (String name, String owner, Library library) {
        this.name = name;
        this.songs = new ArrayList<>();
        this.visibility = "public";
        this.followers = 0;
        this.owner = owner;
        noOfPlaylists++;
        this.ID = noOfPlaylists;
        Users user = getUser(owner, library);
        int noOfPlaylist = user.getNoOfPlaylists();
        noOfPlaylist++;
        this.usersID = noOfPlaylist;
        user.setNoOfPlaylists(noOfPlaylist);
    }

    // Copy constructor with shuffling and seed
    public Playlists(Playlists original, long seed) {
        this.name = original.name;
        this.visibility = original.visibility;
        this.followers = original.followers;
        this.owner = original.owner;
        this.ID = original.ID;
        this.usersID = original.usersID;

        // Create a new ArrayList for the shuffled songs
        this.songs = new ArrayList<>(original.songs);
        shuffleSongs(seed);
    }

    // Helper method to shuffle songs with a given seed
    private void shuffleSongs(long seed) {
        if (this.songs != null) {
            Random random = new Random(seed);
            Collections.shuffle(this.songs, random);
        }
    }

    // Helper function to find a user with a given name
    private Users getUser (String name, Library library) {
        for (Users user : library.getUsers()) {
            if (user.getUsername().equals(name)) {
                return user;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Songs> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Songs> songs) {
        this.songs = songs;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Map<String, Boolean> getUserFollowMap() {
        return userFollowMap;
    }

    public void setUserFollowMap(Map<String, Boolean> userFollowMap) {
        this.userFollowMap = userFollowMap;
    }

    public int getUsersID() {
        return usersID;
    }

    public void setUsersID(int usersID) {
        this.usersID = usersID;
    }
}
