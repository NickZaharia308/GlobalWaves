package commands.users.host;

import com.fasterxml.jackson.databind.JsonNode;
import commands.Command;
import commands.page.observer.PageSubject;
import lombok.Getter;
import main.Library;
import user.entities.Host;
import user.entities.Users;
import user.entities.audio.files.Episodes;
import user.entities.audio.files.Podcasts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * The {@code AddPodcast} class represents a command to add a podcast by a host.
 * It extends the {@link Command} class and is used to process and execute commands
 * related to adding podcasts to the host's collection. The class includes methods
 * to validate the input command, check if a podcast with the same name already exists,
 * add the new podcast to the host's collection, and notify observers. Additionally,
 * it ensures that episodes in the podcast are unique.
 */
@Getter
public class AddPodcast extends Command {
    private String message;

    /**
     * Processes the input command to add a podcast by a host user and notifies observers.
     *
     * @param command The command containing information about the user and
     *                the operation to be performed.
     * @param library The main library containing information about users and entities.
     */
    public void returnAddPodcast(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());
        super.setName(command.getName());
        super.setEpisodes(command.getEpisodes());

        Users user = new Users();
        user = user.getUser(library.getUsers(), this.getUsername());

        if (user == null) {
            setMessage("The username " + this.getUsername() + " doesn't exist.");
            return;
        }

        if (user.getUserType() != Users.UserType.HOST) {
            setMessage(this.getUsername() + " is not a host.");
            return;
        }

        Host host = (Host) user;
        for (Podcasts podcast : host.getPodcasts()) {
            if (podcast.getName().equals(this.getName())) {
                setMessage(this.getUsername() + " has another podcast with the same name.");
                return;
            }
        }

        ArrayList<Episodes> podcastEpisodes = new ArrayList<>();
        for (JsonNode episodeNode : this.getEpisodes()) {

            String name = episodeNode.get("name").asText();
            int duration = episodeNode.get("duration").asInt();
            String description = episodeNode.get("description").asText();

            Episodes episodes = new Episodes(name, duration, description);

            podcastEpisodes.add(episodes);
        }

        // Verify if an episode appears twice
        if (hasDuplicateEpisodes(podcastEpisodes)) {
            setMessage(this.getUsername() + " has the same episode in this podcast.");
            return;
        }

        Podcasts newPodcast = new Podcasts(this.name, this.username, podcastEpisodes);

        // Adding the podcast to both the library and the host
        library.getPodcasts().add(newPodcast);
        host.getPodcasts().add(newPodcast);

        // Adding the podcast to each user
        addPodcastToUsers(library, newPodcast);

        // Notify the observers
        PageSubject pageSubject = new PageSubject();
        pageSubject.notifyObservers(host.getUsername());

        setMessage(this.getUsername() + " has added new podcast successfully.");
    }

    /**
     * Checks if there are duplicate episodes in the provided list.
     *
     * @param podcastEpisodes The list of episodes to check for duplicates.
     * @return {@code true} if duplicate episodes are found, {@code false} otherwise.
     */
    public boolean hasDuplicateEpisodes(final ArrayList<Episodes> podcastEpisodes) {
        Set<String> uniqueNames = new HashSet<>();

        for (Episodes episode : podcastEpisodes) {
            String name = episode.getName();

            // If the name is already in the set, it's a duplicate
            if (!uniqueNames.add(name)) {
                // Duplicate found
                return true;
            }
        }
        // No duplicates found
        return false;
    }

    /**
     * Adds the podcast to the music player of each user in the library.
     *
     * @param library The main library containing information about users and entities.
     * @param podcast The podcast to be added to users' music players.
     */
    public void addPodcastToUsers(final Library library, final Podcasts podcast) {
        ArrayList<Users> users = library.getUsers();
        for (Users user : users) {
            if (user.getMusicPlayer() != null && user.getMusicPlayer().getPodcasts() != null) {
                // Create a new Podcast so they do not interfere
                Podcasts newPodcast = new Podcasts(podcast);
                user.getMusicPlayer().getPodcasts().add(newPodcast);
            }
        }
    }

    /**
     * Sets the message indicating the result of the add podcast operation.
     *
     * @param message The message to be set.
     */
    public void setMessage(final String message) {
        this.message = message;
    }
}
