package main;

import java.util.ArrayList;

public class Playlists {
    private String name;
    private ArrayList<Songs> songs = null;
    private String visibilty;
    private int followers;
    private String owner;
    private int ID;
    static int noOfPlaylists = 0;

    public Playlists (String name, String owner) {
        this.name = name;
        this.songs = new ArrayList<>();
        this.visibilty = "public";
        this.followers = 0;
        this.owner = owner;
        noOfPlaylists++;
        this.ID = noOfPlaylists;

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

    public String getVisibilty() {
        return visibilty;
    }

    public void setVisibilty(String visibilty) {
        this.visibilty = visibilty;
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
}
