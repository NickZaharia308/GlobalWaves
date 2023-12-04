package userEntities;

import lombok.Getter;
import userEntities.audio.Album;
import userEntities.specialEntities.Event;
import userEntities.specialEntities.Merch;

import java.util.ArrayList;

@Getter
public class Artist extends Users {

    private ArrayList<Album> albums = new ArrayList<>();
    private ArrayList<Event> events = new ArrayList<>();
    private ArrayList<Merch> merchandise = new ArrayList<>();

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

    public void setAlbums(ArrayList<Album> albums) {
        this.albums = albums;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public void setMerchandise(ArrayList<Merch> merchandise) {
        this.merchandise = merchandise;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Albums:\n");

        // Append each album name to the string
        for (Album album : albums) {
            builder.append("\t[").append(album.getName()).append("]\n");
        }

        // Merch section
        builder.append("\nMerch:\n\t");
        if (merchandise.isEmpty()) {
            builder.append("[]");
        } else {
            for (Merch merch : merchandise) {
                if (merch == merchandise.get(0)) {
                    builder.append("[").append(merch.getName()).append(" - ").append(merch.getPrice())
                            .append(":\n\t").append(merch.getDescription());
                } else {
                    builder.append(", ").append(merch.getName()).append(" - ").append(merch.getPrice())
                            .append(":\n\t").append(merch.getDescription()).append("]");
                }
            }
        }

        // Events section
        builder.append("\n\nEvents:\n\t");
        for (Event event : events) {
            if (event == events.get(0)) {
                builder.append("[").append(event.getName()).append(" - ").append(event.getDate())
                        .append(":\n\t").append(event.getDescription());
            } else {
                builder.append(", ").append(event.getName()).append(" - ").append(event.getDate())
                        .append(":\n\t").append(event.getDescription()).append("]");
            }
        }

        return builder.toString();
    }

}
