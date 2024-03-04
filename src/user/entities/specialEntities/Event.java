package user.entities.specialEntities;

import lombok.Getter;
import lombok.Setter;

/**
 * The Event class represents an event with a name, description, and date.
 * An event is created by an artist.
 */
@Getter
@Setter
public class Event {

    private String name;
    private String description;
    private String date;

    /**
     * Constructs a new Event object with the specified name, description, and date.
     *
     * @param name        The name of the event.
     * @param description The description of the event.
     * @param date        The date of the event.
     */
    public Event(final String name, final String description, final String date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }
}
