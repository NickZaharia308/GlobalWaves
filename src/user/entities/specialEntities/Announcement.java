package user.entities.specialEntities;

import lombok.Getter;
import lombok.Setter;

/**
 * The Announcement class represents an announcement entity with a name and description.
 * An announcement is created by a host.
 */
@Getter
@Setter
public class Announcement {
    private String name;
    private String description;

    /**
     * Constructs a new Announcement object with the specified name and description.
     *
     * @param name        The name of the announcement.
     * @param description The description of the announcement.
     */
    public Announcement(final String name, final String description) {
        this.name = name;
        this.description = description;
    }
}
