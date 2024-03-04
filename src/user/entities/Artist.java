package user.entities;

import commands.users.notifications.NotificationObserver;
import commands.users.notifications.NotificationSubject;
import lombok.Getter;
import lombok.Setter;
import main.Library;
import user.entities.audio.files.Songs;
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
@Setter
public class Artist extends Users implements NotificationSubject {

    // Wrapped
    private ArrayList<Album> albums = new ArrayList<>();
    private ArrayList<Event> events = new ArrayList<>();
    private ArrayList<Merch> merchandise = new ArrayList<>();
    private Map<String, Integer> topFans = new HashMap<>();
    private Map<String, Boolean> listeners = new HashMap<>();
    // Monetization
    private double merchRevenue;
    private double songRevenue;
    private int ranking = 1;
    private String mostProfitableSong = new String("N/A");
    private int addOnPlatformOrder;
    private static int order = 0;
    private Map<Songs, Double> songsRevenues = new HashMap<>();
    // Subscribers
    private ArrayList<NotificationObserver> observers = new ArrayList<>();
    private String notificationName;
    private String notificationDescription;



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
        this.addOnPlatformOrder = order;
        order++;
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

    /**
     * Checks if the artist has had at least one user that listened its music.
     *
     * @return true if the artist has at least one listener, false otherwise.
     */
    public boolean hasTrueValueInListeners() {
        for (boolean value : listeners.values()) {
            if (value) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the artist has a listener (normal user) with the specified username.
     * @param artistName The name of the artist.
     * @param user The user to be checked.
     * @return true if the user is a listener of the artist, false otherwise.
     */
    public static boolean hasObserverInSubscribers(final String artistName, final Users user) {
        Artist artist = (Artist) user.getUser(Library.getInstance().getUsers(), artistName);
        if (artist.getObservers().contains(user)) {
            return true;
        }
        return false;
    }

    /**
     * Adds a notification observer to the list of notification observers.
     *
     * @param notificationObserver The notification observer to add.
     */
    @Override
    public void addNotificationObserver(final NotificationObserver notificationObserver) {
        observers.add(notificationObserver);
    }

    /**
     * Removes a notification observer from the list of notification observers.
     *
     * @param notificationObserver The notification observer to remove.
     */
    @Override
    public void removeNotificationObserver(final NotificationObserver notificationObserver) {
        observers.remove(notificationObserver);
    }

    /**
     * Notifies all notification observers that the artist has a new event.
     */
    @Override
    public void notifyNotificationObservers() {
        for (NotificationObserver observer : observers) {
            observer.update(this);
        }
    }
}
