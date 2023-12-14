package user.entities;

import lombok.Getter;
import user.entities.audio.files.Episodes;
import user.entities.specialEntities.Announcement;
import user.entities.audio.files.Podcasts;

import java.util.ArrayList;


/**
 * Represents a Host entity in the library system.
 * Extends the Users (It is a special type of user) class and includes information about podcasts
 * and announcements associated with the host.
 */
@Getter
public class Host extends Users {

    private ArrayList<Podcasts> podcasts = new ArrayList<>();
    private ArrayList<Announcement> announcements = new ArrayList<>();

    /**
     * Constructs a new Host object with the specified username, age, and city.
     *
     * @param username The username of the host.
     * @param age      The age of the host.
     * @param city     The city where the host resides.
     */
    public Host(final String username, final int age, final String city) {
        super.username = username;
        super.age = age;
        super.city = city;
    }

    /**
     * Sets the list of podcasts for the host.
     *
     * @param podcasts The list of podcasts to set.
     */
    public void setPodcasts(final ArrayList<Podcasts> podcasts) {
        this.podcasts = new ArrayList<>(podcasts);
    }

    /**
     * Sets the list of announcements for the host.
     *
     * @param announcements The list of announcements to set.
     */
    public void setAnnouncements(final ArrayList<Announcement> announcements) {
        this.announcements = new ArrayList<>(announcements);
    }

    /**
     * Overrides the toString method to provide a string representation of the Host.
     * It is used in the command "PrintCurrentPage"
     *
     * @return String representation of the Host's page.
     */
    @Override
    public String toString() {
        // Podcasts section
        StringBuilder builder = new StringBuilder("Podcasts:\n\t");

        if (podcasts.isEmpty()) {
            builder.append("[]");
        } else {
            for (Podcasts podcast : podcasts) {
                if (podcast == podcasts.get(0)) {
                    builder.append("[").append(podcast.getName()).append(":\n\t");
                } else {
                    builder.append("\n, ").append(podcast.getName()).append(":\n\t");
                }

                // Append episodes for the current podcast
                for (Episodes episode : podcast.getEpisodes()) {
                    if (episode == podcast.getEpisodes().get(0)) {
                        builder.append("[").append(episode.getName()).append(" - ")
                                .append(episode.getDescription());
                    } else if (episode == podcast.getEpisodes()
                            .get(podcast.getEpisodes().size() - 1)
                            && podcast == podcasts.get(podcasts.size() - 1)) {
                        builder.append(", ").append(episode.getName()).append(" - ")
                                .append(episode.getDescription());
                        builder.append("]\n");
                    } else {
                        builder.append(", ").append(episode.getName()).append(" - ")
                                .append(episode.getDescription());
                    }
                }

                builder.append("]");
            }
        }

        // Announcements section
        builder.append("\n\nAnnouncements:\n\t");
        if (announcements.isEmpty()) {
            builder.append("[]");
        } else {
            for (Announcement announcement : announcements) {
                if (announcement == announcements.get(0)) {
                    builder.append("[").append(announcement.getName()).append(":\n\t")
                            .append(announcement.getDescription());
                    builder.append("\n");
                } else {
                    builder.append(", ").append(announcement.getName()).append(":\n\t")
                            .append(announcement.getDescription());
                }
            }
            builder.append("]");
        }

        return builder.toString();
    }
}

