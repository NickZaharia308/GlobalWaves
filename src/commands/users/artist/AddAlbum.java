package commands.users.artist;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import commands.page.Subject;
import lombok.Getter;
import main.Library;
import user.entities.Artist;
import user.entities.Users;
import user.entities.audio.files.Album;
import user.entities.audio.files.Songs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * The {@code AddAlbum} class represents a command to add a new album to an artist's
 * profile and the library.
 * It extends the {@link commands.Command} class and includes methods to validate and process
 * the addition of an album.
 * The class ensures that the album is not a duplicate and notifies observers (Users)
 * after a successful addition.
 */
@Getter
public class AddAlbum extends Command {
    private String message;

    /**
     * Adds a new album to the artist's profile and the library.
     *
     * @param command The command containing album information.
     * @param library The library containing user and album data.
     */
    public void returnAddAlbum(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());
        super.setName(command.getName());
        super.setReleaseYear(command.getReleaseYear());
        super.setDescription(command.getDescription());
        super.setSongs(command.getSongs());

        Users user = new Users();
        user = user.getUser(library.getUsers(), this.getUsername());

        if (user == null) {
            setMessage("The username " + this.getUsername() + " doesn't exist.");
            return;
        }

        if (user.getUserType() != Users.UserType.ARTIST) {
            setMessage(this.getUsername() + " is not an artist.");
            return;
        }

        Artist artist = (Artist) user;
        for (Album album : artist.getAlbums()) {
            if (album.getName().equals(this.getName())) {
                setMessage(this.getUsername() + " has another album with the same name.");
                return;
            }
        }

        ArrayList<Songs> albumSongs = new ArrayList<>();
        for (JsonNode songNode : this.getSongs()) {
            String name = songNode.get("name").asText();
            int duration = songNode.get("duration").asInt();
            String album = songNode.get("album").asText();

            // Access the tags and create the tags list
            ArrayNode tagsArray = (ArrayNode) songNode.get("tags");
            ArrayList<String> tags = new ArrayList<>();
            tagsArray.forEach(tag -> tags.add(tag.asText()));

            String lyrics = songNode.get("lyrics").asText();
            String genre = songNode.get("genre").asText();
            int releaseYear = songNode.get("releaseYear").asInt();
            String owner = songNode.get("artist").asText();

            Songs song = new Songs(name, duration, album, tags, lyrics, genre, releaseYear, owner);

            // Add the song in album
            albumSongs.add(song);
        }

        // Verify if a song appears twice
        if (hasDuplicateSongs(albumSongs)) {
            setMessage(this.getUsername() + " has the same song at least twice in this album.");
            return;
        }

        // If there are no duplicates, add the songs to the library too
        for (Songs addedSong : albumSongs) {
            library.getSongs().add(addedSong);
        }

        Album newAlbum = new Album(this.name, this.releaseYear, this.description,
                                    albumSongs, this.username);

        // Adding the album to both the library and the artist
        library.getAlbums().add(newAlbum);
        artist.getAlbums().add(newAlbum);

        // Notify the observers
        Subject subject = new Subject();
        subject.notifyObservers(artist.getUsername());

        setMessage(this.getUsername() + " has added new album successfully.");
    }

    /**
     * Checks if there are duplicate songs in the given album.
     *
     * @param albumSongs The list of songs in the album.
     * @return True if there are duplicate songs, false otherwise.
     */
    public boolean hasDuplicateSongs(final ArrayList<Songs> albumSongs) {
        Set<String> uniqueNames = new HashSet<>();

        for (Songs song : albumSongs) {
            String name = song.getName();

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
     * Sets the message for the current instance.
     *
     * @param message The message to be set.
     */
    public void setMessage(final String message) {
        this.message = message;
    }
}
