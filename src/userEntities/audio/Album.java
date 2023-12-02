package userEntities.audio;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Album {
    private String name;
    private int releaseYear;
    private String description;
    private ArrayList<Songs> songs = null;
    private String owner;

    public Album(final String name, final int releaseYear, final String description,
                 final ArrayList<Songs> songs, final String username) {
        this.name = name;
        this.releaseYear = releaseYear;
        this.description = description;
        this.songs = songs;
        this.owner = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSongs(ArrayList<Songs> songs) {
        this.songs = songs;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
