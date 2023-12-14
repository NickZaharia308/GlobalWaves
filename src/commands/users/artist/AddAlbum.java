package commands.users.artist;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import commands.page.Subject;
import lombok.Getter;
import main.Library;
import userEntities.Artist;
import userEntities.Users;
import userEntities.audio.Album;
import userEntities.audio.Songs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Getter
public class AddAlbum extends Command {
    private String message;

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

        Album newAlbum = new Album(this.name, this.releaseYear, this.description, albumSongs, this.username);

        // Adding the album to both the library and the artist
        library.getAlbums().add(newAlbum);
        artist.getAlbums().add(newAlbum);

        // Notify the observers
        Subject subject = new Subject();
        subject.notifyObservers(artist.getUsername());

        setMessage(this.getUsername() + " has added new album successfully.");
    }

    public boolean hasDuplicateSongs(ArrayList<Songs> albumSongs) {
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

    public void setMessage(String message) {
        this.message = message;
    }
}
