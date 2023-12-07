package userEntities.audio;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

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

    // Copy constructor
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
