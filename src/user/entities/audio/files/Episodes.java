package user.entities.audio.files;

import lombok.Getter;

/**
 * The Episodes class represents an episode in the system.
 */
@Getter
public class Episodes {
    private String name;
    private int duration;
    private String description;
    private int remainingTime;

    /**
     * Constructs a new Episodes object with the specified attributes.
     *
     * @param name        The name of the episode.
     * @param duration    The duration of the episode.
     * @param description The description of the episode.
     */
    public Episodes(final String name, final int duration, final String description) {
        this.name = name;
        this.duration = duration;
        this.description = description;
        this.remainingTime = duration;
    }

    /**
     * Copy constructor for creating a new Episodes object from an existing one.
     *
     * @param otherEpisode The original Episodes object to be copied.
     */
    public Episodes(final Episodes otherEpisode) {
        this.name = otherEpisode.name;
        this.duration = otherEpisode.duration;
        this.description = otherEpisode.description;
        this.remainingTime = otherEpisode.remainingTime;
    }

    /**
     * Sets the name of the episode.
     *
     * @param name The new name of the episode.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Sets the duration of the episode.
     *
     * @param duration The new duration of the episode.
     */
    public void setDuration(final int duration) {
        this.duration = duration;
    }

    /**
     * Sets the description of the episode.
     *
     * @param description The new description of the episode.
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Sets the remaining time of the episode.
     *
     * @param remainingTime The new remaining time of the episode.
     */
    public void setRemainingTime(final int remainingTime) {
        this.remainingTime = remainingTime;
    }
}
