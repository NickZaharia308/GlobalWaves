package main;

import java.util.ArrayList;

public class Library {
    private ArrayList<Users> users;
    private ArrayList<Songs> songs;
    private ArrayList<Podcasts> podcasts;
    private ArrayList<Playlists> playlists;

    public ArrayList<Users> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<Users> users) {
        this.users = users;
    }

    public ArrayList<Songs> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Songs> songs) {
        this.songs = songs;
    }

    public ArrayList<Podcasts> getPodcasts() {
        return podcasts;
    }

    public void setPodcasts(ArrayList<Podcasts> podcasts) {
        this.podcasts = podcasts;
    }

    public ArrayList<Playlists> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(ArrayList<Playlists> playlists) {
        this.playlists = playlists;
    }
}
