package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;

/**
 * The {@code Command} class represents a user command with various parameters.
 * It is used as a body for the rest of the Classes that have the fields of Command
 * and some other.
 * It has all the methods and fields that a command could require for input
 */
@Getter
public class Command {
    protected String command;
    protected String username;
    protected int timestamp;
    protected int seed;
    protected String type;
    protected String playlistName;
    protected JsonNode filters;
    protected int itemNumber;
    protected int playlistId;
    protected int age;
    protected String city;
    protected String name;
    protected String description;
    protected ArrayNode songs;
    protected ArrayNode episodes;
    protected int releaseYear;
    protected String date;
    protected int price;
    protected String nextPage;
    protected String recommendationType;


    /**
     * Sets the command for the current instance.
     *
     * @param command The command to be set.
     */
    public void setCommand(final String command) {
        this.command = command;
    }

    /**
     * Sets the username for the current instance.
     *
     * @param username The username to be set.
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Sets the timestamp for the current instance.
     *
     * @param timestamp The timestamp to be set.
     */
    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Sets the seed for the current instance.
     *
     * @param seed The seed to be set.
     */
    public void setSeed(final int seed) {
        this.seed = seed;
    }

    /**
     * Sets the type for the current instance.
     *
     * @param type The type to be set.
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * Sets the playlist name for the current instance.
     *
     * @param playlistName The playlist name to be set.
     */
    public void setPlaylistName(final String playlistName) {
        this.playlistName = playlistName;
    }

    /**
     * Sets the filters for the current instance.
     *
     * @param filters The filters to be set.
     */
    public void setFilters(final JsonNode filters) {
        this.filters = filters;
    }

    /**
     * Sets the item number for the current instance.
     *
     * @param itemNumber The item number to be set.
     */
    public void setItemNumber(final int itemNumber) {
        this.itemNumber = itemNumber;
    }

    /**
     * Sets the playlist ID for the current instance.
     *
     * @param playlistId The playlist ID to be set.
     */
    public void setPlaylistId(final int playlistId) {
        this.playlistId = playlistId;
    }

    /**
     * Sets the age for the current instance.
     *
     * @param age The age to be set.
     */
    public void setAge(final int age) {
        this.age = age;
    }

    /**
     * Sets the city for the current instance.
     *
     * @param city The city to be set.
     */
    public void setCity(final String city) {
        this.city = city;
    }

    /**
     * Sets the name for the current instance.
     *
     * @param name The name to be set.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Sets the description for the current instance.
     *
     * @param description The description to be set.
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Sets the songs for the current instance.
     *
     * @param songs The songs to be set.
     */
    public void setSongs(final ArrayNode songs) {
        this.songs = songs;
    }

    /**
     * Sets the episodes for the current instance.
     *
     * @param episodes The episodes to be set.
     */
    public void setEpisodes(final ArrayNode episodes) {
        this.episodes = episodes;
    }

    /**
     * Sets the release year for the current instance.
     *
     * @param releaseYear The release year to be set.
     */
    public void setReleaseYear(final int releaseYear) {
        this.releaseYear = releaseYear;
    }

    /**
     * Sets the date for the current instance.
     *
     * @param date The date to be set.
     */
    public void setDate(final String date) {
        this.date = date;
    }

    /**
     * Sets the price for the current instance.
     *
     * @param price The price to be set.
     */
    public void setPrice(final int price) {
        this.price = price;
    }

    /**
     * Sets the next page for the current instance.
     *
     * @param nextPage The next page to be set.
     */
    public void setNextPage(final String nextPage) {
        this.nextPage = nextPage;
    }
}
