package user.entities.specialEntities;

import lombok.Getter;

/**
 * The Announcement class represents an announcement entity with a name and description.
 * An announcement is created by a host.
 */
@Getter
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

    /**
     * Sets the name of the announcement.
     *
     * @param name The new name to be set for the announcement.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Sets the description of the announcement.
     *
     * @param description The new description to be set for the announcement.
     */
    public void setDescription(final String description) {
        this.description = description;
    }
}
