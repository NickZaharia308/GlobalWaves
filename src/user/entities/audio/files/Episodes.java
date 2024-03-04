package user.entities.audio.files;

import lombok.Getter;
import lombok.Setter;

/**
 * The Episodes class represents an episode in the system.
 */
@Getter
@Setter
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
}
