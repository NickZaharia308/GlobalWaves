package main;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;

import java.util.ArrayList;

/**
 * The {@code Command} class represents a user command with various parameters.
 * It is used as a body for the rest of the Classes that have the fields of Command
 * and some other.
 * It has all the methods and fields that a command could require for input
 */
@Getter
public class Command {
    private String command;
    private String username;
    private int timestamp;
    private int seed;
    private String type;
    private String playlistName;
    private JsonNode filters;
    private int itemNumber;
    private int playlistId;
    private int age;
    private String city;
    private String name;
    private String description;
    private ArrayNode songs;
    private ArrayNode episodes;
    private int releaseYear;
    private String date;
    private int price;
    private String nextPage;

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


    public void setAge(int age) {
        this.age = age;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
