package commands.users.artist;

import commands.Command;
import commands.page.Subject;
import lombok.Getter;
import main.Library;
import userEntities.Artist;
import userEntities.Users;
import userEntities.specialEntities.Event;

import java.util.ArrayList;

@Getter
public class RemoveEvent extends Command {
    private String message;

    public void returnRemoveEvent(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        Users user = new Users();
        user = user.getUser(library.getUsers(), this.getUsername());

        if (user == null) {
            setMessage("The username " + this.getUsername() + " doesn't exist.");
            return;
        }

        if (user.getUserType() != Users.UserType.ARTIST) {
            setMessage(this.getUsername() + " is not an artist.");
            return;
        }

        Artist artist = (Artist) user;

        ArrayList<Event> events = artist.getEvents();
        Event eventToDelete = null;
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
        Subject subject = new Subject();
        subject.notifyObservers(artist.getUsername());

        setMessage(this.getUsername() + " deleted the event successfully.");
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
