package commands.users.host;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import lombok.Getter;
import main.Library;
import userEntities.Artist;
import userEntities.Host;
import userEntities.Users;
import userEntities.audio.Album;
import userEntities.audio.Episodes;
import userEntities.audio.Podcasts;
import userEntities.audio.Songs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Getter
public class AddPodcast extends Command {
    private String message;

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

        // Adding the podcast to both the library and the artist
        library.getPodcasts().add(newPodcast);
        host.getPodcasts().add(newPodcast);

        // Adding the podcast to each user
        addPodcastToUsers(library, newPodcast);

        setMessage(this.getUsername() + " has added new podcast successfully.");
    }

    public boolean hasDuplicateEpisodes(ArrayList<Episodes> podcastEpisodes) {
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

    public void addPodcastToUsers(final Library library, Podcasts podcast) {
        ArrayList<Users> users = library.getUsers();
        for (Users user : users) {
            if (user.getMusicPlayer() != null && user.getMusicPlayer().getPodcasts() != null) {
                user.getMusicPlayer().getPodcasts().add(podcast);
            }
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
