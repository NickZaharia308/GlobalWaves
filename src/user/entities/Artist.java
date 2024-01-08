package user.entities;

import lombok.Getter;
import user.entities.specialEntities.Merch;
import user.entities.audio.files.Album;
import user.entities.specialEntities.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an Artist entity in the music library system.
 * Extends the Users (It is a special type of user) class and includes information
 * about albums, events and merchandise associated with the artist.
 */
@Getter
public class Artist extends Users {

    private ArrayList<Album> albums = new ArrayList<>();
    private ArrayList<Event> events = new ArrayList<>();
    private ArrayList<Merch> merchandise = new ArrayList<>();
    private Map<String, Integer> topFans = new HashMap<>();

    /**
     * Constructs a new Artist object with the specified username, age, and city.
     *
     * @param username The username of the user.
     * @param age      The age of the user.
     * @param city     The city where the user resides.
     */
    public Artist(final String username, final int age, final String city) {
        super(username, age, city);
        super.setOnline(false);
    }

    /**
     * Sets the list of albums for the artist.
     *
     * @param albums The list of albums to set.
     */
    public void setAlbums(final ArrayList<Album> albums) {
        this.albums = new ArrayList<>(albums);
    }

    /**
     * Sets the list of events for the artist.
     *
     * @param events The list of events to set.
     */
    public void setEvents(final ArrayList<Event> events) {
        this.events = new ArrayList<>(events);
    }

    /**
     * Sets the list of merchandise for the artist.
     *
     * @param merchandise The list of merchandise to set.
     */
    public void setMerchandise(final ArrayList<Merch> merchandise) {
        this.merchandise = new ArrayList<>(merchandise);
    }

    /**
     * Overrides the toString method to provide a string representation of the Artist.
     * It is used in the command "PrintCurrentPage"
     *
     * @return String representation of the Artist's page.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        // Album section
        builder.append("Albums:\n\t");
        if (albums.isEmpty()) {
            builder.append("[]");
        } else {
            for (Album album : albums) {
                if (album == albums.get(0)) {
                    builder.append("[").append(album.getName());
                } else {
                    builder.append(", ").append(album.getName());
                }
            }
            builder.append("]");
        }

        // Merch section
        builder.append("\n\nMerch:\n\t");
        if (merchandise.isEmpty()) {
            builder.append("[]");
        } else {
            for (Merch merch : merchandise) {
                if (merch == merchandise.get(0)) {
                    builder.append("[").append(merch.getName()).append(" - ")
                    .append(merch.getPrice()).append(":\n\t").append(merch.getDescription());
                } else {
                    builder.append(", ").append(merch.getName()).append(" - ")
                    .append(merch.getPrice()).append(":\n\t").append(merch.getDescription());
                }
            }
            builder.append("]");
        }

        // Events section
        builder.append("\n\nEvents:\n\t");
        if (events.isEmpty()) {
            builder.append("[]");
        } else {
            for (Event event : events) {
                if (event == events.get(0)) {
                    builder.append("[").append(event.getName()).append(" - ")
                    .append(event.getDate());
                    builder.append(":\n\t").append(event.getDescription());
                } else {
                    builder.append(", ").append(event.getName()).append(" - ")
                    .append(event.getDate());
                    builder.append(":\n\t").append(event.getDescription());
                }
            }
            builder.append("]");
        }

        return builder.toString();
    }
}
