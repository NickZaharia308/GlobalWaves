package main;

import lombok.Getter;

import java.util.ArrayList;

/**
 * The Podcasts class represents a podcast in the system.
 */
@Getter
public class Podcasts {
    private String name;
    private String owner;
    private ArrayList<Episodes> episodes;

    /**
     * Constructs a new Podcasts object with the specified attributes.
     *
     * @param name     The name of the podcast.
     * @param owner    The owner of the podcast.
     * @param episodes The list of episodes associated with the podcast.
     */
    public Podcasts(final String name, final String owner, final ArrayList<Episodes> episodes) {
        this.name = name;
        this.owner = owner;
        this.episodes = episodes;
    }

    /**
     * Copy constructor for creating a new Podcasts object from an existing one.
     *
     * @param otherPodcast The original Podcasts object to be copied.
     */
    public Podcasts(final Podcasts otherPodcast) {
        this.name = otherPodcast.name;
        this.owner = otherPodcast.owner;

        // Create a new ArrayList and copy the episodes
        this.episodes = new ArrayList<>();
        for (Episodes episode : otherPodcast.episodes) {
            this.episodes.add(new Episodes(episode));
        }
    }

    /**
     * Resets the remaining time of all episodes in the podcast to their full duration.
     *
     * @param podcast The podcast for which episodes should be reset.
     */
    public void resetEpisodes(final Podcasts podcast) {
        for (Episodes episode : podcast.getEpisodes()) {
            episode.setRemainingTime(episode.getDuration());
        }
    }

    /**
     * Sets the name of the podcast.
     *
     * @param name The new name of the podcast.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Sets the owner of the podcast.
     *
     * @param owner The new owner of the podcast.
     */
    public void setOwner(final String owner) {
        this.owner = owner;
    }

    /**
     * Sets the list of episodes associated with the podcast.
     *
     * @param episodes The new list of episodes for the podcast.
     */
    public void setEpisodes(final ArrayList<Episodes> episodes) {
        this.episodes = episodes;
    }
}
