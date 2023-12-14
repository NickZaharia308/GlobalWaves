package commands.users.artist;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import commands.Command;
import commands.page.Subject;
import lombok.Getter;
import main.Library;
import userEntities.Artist;
import userEntities.Users;
import userEntities.specialEntities.Event;

import java.util.ArrayList;

@Getter
public class AddEvent extends Command {
    private String message;

    public void returnAddEvent(final Command command, final Library library) {
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

        for (Event event : events) {
            if (event.getName().equals(command.getName())) {
                setMessage(this.getUsername() + " has another event with the same name.");
                return;
            }
        }

        try {
            validateEventDate(this.getUsername(), command.getDate());
        } catch (IllegalArgumentException e) {
            setMessage(e.getMessage());
            return;
        }

        // Create the new event and add it to the artist's event array
        Event event = new Event(command.getName(), command.getDescription(), command.getDate());
        artist.getEvents().add(event);

        // Notify the observers
        Subject subject = new Subject();
        subject.notifyObservers(artist.getUsername());

        setMessage(this.getUsername() + " has added new event successfully.");
    }


    public static void validateEventDate(String username, String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            LocalDate date = LocalDate.parse(dateString, formatter);

            int day = date.getDayOfMonth();
            int month = date.getMonthValue();
            int year = date.getYear();

            if (day > 28 && month == 2) {
                throw new IllegalArgumentException("Event for " + username + " does not have a valid date.");
            }

            if (day > 31 || month > 12 || year < 1900 || year > 2023) {
                throw new IllegalArgumentException("Event for " + username + " does not have a valid date.");
            }

        } catch (Exception e) {
            throw new IllegalArgumentException("Event for " + username + " does not have a valid date.");
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
