package commands.users.artist;

import commands.Command;
import commands.page.PageSubject;
import lombok.Getter;
import main.Library;
import user.entities.Artist;
import user.entities.Users;
import user.entities.specialEntities.Event;

import java.util.ArrayList;

/**
 * The {@code RemoveEvent} class represents a command to remove an event for an artist.
 * It extends the {@code Command} class and is specific to artist-related operations.
 * The class notifies observers (Users) after a successful removal.
 */
@Getter
public class RemoveEvent extends Command {
    private String message;

    /**
     * Processes the remove event command, removing the specified event for the artist.
     *
     * @param command The command containing details about the event removal.
     * @param library The main library containing user data.
     */
    public void returnRemoveEvent(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        Users user = new Users();
        user = user.getUser(library.getUsers(), this.getUsername());

        // Check if the user exists
        if (user == null) {
            setMessage("The username " + this.getUsername() + " doesn't exist.");
            return;
        }

        // Check if the user is an artist
        if (user.getUserType() != Users.UserType.ARTIST) {
            setMessage(this.getUsername() + " is not an artist.");
            return;
        }

        Artist artist = (Artist) user;

        ArrayList<Event> events = artist.getEvents();
        Event eventToDelete = null;

        // Find the event to delete
        for (Event event : events) {
            if (event.getName().equals(command.getName())) {
                eventToDelete = event;
                break;
            }
        }

        // If there is no event with the given name
        if (eventToDelete == null) {
            setMessage(this.getUsername() + " doesn't have an event with the given name.");
            return;
        }

        // Delete event
        artist.getEvents().remove(eventToDelete);

        // Notify the observers
        PageSubject pageSubject = new PageSubject();
        pageSubject.notifyObservers(artist.getUsername());

        setMessage(this.getUsername() + " deleted the event successfully.");
    }

    /**
     * Sets the message for this RemoveEvent instance.
     *
     * @param message The message to be set.
     */
    public void setMessage(final String message) {
        this.message = message;
    }
}
