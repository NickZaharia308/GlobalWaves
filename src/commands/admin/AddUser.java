package commands.admin;

import commands.Command;
import lombok.Getter;
import main.Library;
import user.entities.Artist;
import user.entities.Host;
import user.entities.Users;

import java.util.ArrayList;

/**
 * The AddUser class represents a command to add a new user
 * (either Artist, Host, or Normal User) to the system.
 */
@Getter
public class AddUser extends Command {
    private String message;

    /**
     * Adds a new user to the system based on the provided command and
     * updates the message accordingly.
     *
     * @param command The command containing information about the user to be added.
     * @param library The main library containing user data.
     */
    public void returnAddUser(final Command command, final Library library) {
        // Set command-related information
        super.setCommand(command.getCommand());
        super.setTimestamp(command.getTimestamp());
        super.setUsername(command.getUsername());

        ArrayList<Users> allUsers = library.getUsers();

        // Check if the user already exists
        Users user = new Users();
        user = user.getUser(allUsers, this.getUsername());
        if (user != null) {
            setMessage("The username " + this.getUsername() + " is already taken.");
            return;
        }

        // Add the user based on its type
        if (command.getType().equalsIgnoreCase(Users.UserType.ARTIST.toString())) {
            Artist newArtist = new Artist(getUsername(), command.getAge(), command.getCity());
            newArtist.setUserType(Users.UserType.ARTIST);
            allUsers.add(newArtist);
        } else if (command.getType().equalsIgnoreCase(Users.UserType.HOST.toString())) {
            Host newHost = new Host(getUsername(), command.getAge(), command.getCity());
            newHost.setUserType(Users.UserType.HOST);
            allUsers.add(newHost);
        } else {
            Users newUser = new Users(getUsername(), command.getAge(), command.getCity());
            allUsers.add(newUser);
        }

        setMessage("The username " + getUsername() + " has been added successfully.");
    }

    /**
     * Sets the message related to the execution of the AddUser command.
     *
     * @param message The message to be set.
     */
    public void setMessage(final String message) {
        this.message = message;
    }
}
