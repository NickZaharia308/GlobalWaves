package user.entities.specialEntities;

import lombok.Getter;

/**
 * The Event class represents an event with a name, description, and date.
 * An event is created by an artist.
 */
@Getter
public class Event {

    /**
     * The name of the event.
     */
    private String name;

    /**
     * The description of the event.
     */
    private String description;

    /**
     * The date of the event.
     */
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

    /**
     * Sets the name of the event.
     *
     * @param name The name to set.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Sets the description of the event.
     *
     * @param description The description to set.
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Sets the date of the event.
     *
     * @param date The date to set.
     */
    public void setDate(final String date) {
        this.date = date;
    }
}
