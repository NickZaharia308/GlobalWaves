package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;

/**
 * The {@code Command} class represents a user command with various parameters.
 * It is used as a body for the rest of the Classes that have the fields of Command
 * and some other.
 * It has all the methods and fields that a command could require for input
 */
@Getter
@Setter
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
}
