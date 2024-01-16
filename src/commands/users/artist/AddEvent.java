package commands.users.artist;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import commands.Command;
import commands.page.observer.PageSubject;
import lombok.Getter;
import main.Library;
import user.entities.Artist;
import user.entities.Users;
import user.entities.specialEntities.Event;

import java.util.ArrayList;

/**
 * The {@code AddEvent} class represents a command to add a new event to an artist's profile.
 * It extends the {@link commands.Command} class and includes methods to validate and process
 * the addition of an event.
 * The class ensures that the event is not a duplicate, has a valid date, and notifies observers
 * after a successful addition.
 */
@Getter
public class AddEvent extends Command {
    private String message;

    /**
     * Adds a new event to the artist's profile.
     *
     * @param command The command containing event information.
     * @param library The library containing user and event data.
     */
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

        artist.setNotificationName("New Event");
        artist.setNotificationDescription("New Event from " + artist.getUsername() + ".");

        // Notify the notification observers
        artist.notifyNotificationObservers();

        // Notify the observers
        PageSubject pageSubject = new PageSubject();
        pageSubject.notifyObservers(artist.getUsername());

        setMessage(this.getUsername() + " has added new event successfully.");
    }

    /**
     * Validates the date of the event to ensure it is within a valid range.
     *
     * @param username   The username of the artist creating the event.
     * @param dateString The date string to be validated.
     * @throws IllegalArgumentException If the date is not valid.
     */
    public static void validateEventDate(final String username, final String dateString)
            throws IllegalArgumentException {
        final int daysInFebruary = 28;
        final int maxDay = 31;
        final int maxMonth = 12;
        final int minYear = 1900;
        final int maxYear = 2023;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            LocalDate date = LocalDate.parse(dateString, formatter);

            int day = date.getDayOfMonth();
            int month = date.getMonthValue();
            int year = date.getYear();

            if (day > daysInFebruary && month == 2) {
                throw new IllegalArgumentException("Event for "
                        + username + " does not have a valid date.");
            }

            if (day > maxDay || month > maxMonth || year < minYear || year > maxYear) {
                throw new IllegalArgumentException("Event for "
                        + username + " does not have a valid date.");
            }

        } catch (Exception e) {
            throw new IllegalArgumentException("Event for "
                    + username + " does not have a valid date.");
        }
    }

    /**
     * Sets the message for the current instance.
     *
     * @param message The message to be set.
     */
    public void setMessage(final String message) {
        this.message = message;
    }
}
