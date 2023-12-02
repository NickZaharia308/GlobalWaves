package userEntities;

import lombok.Getter;
import userEntities.audio.Album;

import java.util.ArrayList;

@Getter
public class Artist extends Users {

    private ArrayList<Album> albums = new ArrayList<>();

    /**
     * Constructs a new Artist object with the specified username, age, and city.
     *
     * @param username The username of the user.
     * @param age      The age of the user.
     * @param city     The city where the user resides.
     */
    public Artist(final String username, final int age, final String city) {
        super.username = username;
        super.age = age;
        super.city = city;
    }

    public void setAlbums(ArrayList<Album> albums) {
        this.albums = albums;
    }
}
