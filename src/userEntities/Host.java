package userEntities;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Getter;
import userEntities.audio.Episodes;
import userEntities.audio.Podcasts;
import userEntities.specialEntities.Announcement;

import java.util.ArrayList;

@Getter
public class Host extends Users {

    private ArrayList<Podcasts> podcasts = new ArrayList<>();
    private ArrayList<Announcement> announcements = new ArrayList<>();

    /**
     * Constructs a new Artist object with the specified username, age, and city.
     *
     * @param username The username of the user.
     * @param age      The age of the user.
     * @param city     The city where the user resides.
     */
    public Host(final String username, final int age, final String city) {
        super.username = username;
        super.age = age;
        super.city = city;
    }

    public void setPodcasts(ArrayList<Podcasts> podcasts) {
        this.podcasts = podcasts;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Podcasts:\n\t");

        if (podcasts.isEmpty()) {
            builder.append("[]");
        } else {
            for (Podcasts podcast : podcasts) {
                if (podcast == podcasts.get(0)) {
                    builder.append("[").append(podcast.getName()).append(":\n\t");
                } else {
                    builder.append(", ");
                }

                // Append episodes for the current podcast
                for (Episodes episode : podcast.getEpisodes()) {
                    if (episode == podcast.getEpisodes().get(0)) {
                        builder.append("[").append(episode.getName()).append(" - ").append(episode.getDescription());
                    } else {
                        builder.append(", ").append(episode.getName()).append(" - ").append(episode.getDescription());
                    }
                }

                builder.append("]");
            }
        }

        // Announcements section (Assuming announcements is a field in the class)
        builder.append("\n\nAnnouncements:\n\t");
        if (announcements.isEmpty()) {
            builder.append("[]");
        } else {
            for (Announcement announcement : announcements) {
                if (announcement == announcements.get(0)) {
                    builder.append("[").append(announcement.getName()).append(" - ").append(announcement.getDescription());
                } else {
                    builder.append(", ").append(announcement.getName()).append(" - ").append(announcement.getDescription());
                }
            }
            builder.append("]");
        }

        return builder.toString();
    }
}
