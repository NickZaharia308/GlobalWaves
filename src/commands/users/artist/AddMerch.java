package commands.users.artist;

import commands.Command;
import commands.page.observer.PageSubject;
import lombok.Getter;
import main.Library;
import user.entities.Artist;
import user.entities.Users;
import user.entities.specialEntities.Merch;

import java.util.ArrayList;


/**
 * The {@code AddMerch} class represents a command to add merchandise to an artist's collection.
 * It extends the {@link Command} class and is used to process and execute commands
 * related to adding merchandise.
 * The class includes methods to validate the input command, add new merchandise
 * to the artist's collection and notify observers about the update.
 */
@Getter
public class AddMerch extends Command {
    private String message;

    /**
     * Processes the input command to add merchandise and updates the artist's collection.
     *
     * @param command The command containing information about the merchandise to be added.
     * @param library The main library containing information about users and entities.
     */
    public void returnAddMerch(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

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

        ArrayList<Merch> merchandise = artist.getMerchandise();

        for (Merch merch : merchandise) {
            if (merch.getName().equals(command.getName())) {
                setMessage(this.getUsername() + " has merchandise with the same name.");
                return;
            }
        }

        if (command.getPrice() < 0) {
            setMessage("Price for merchandise can not be negative.");
            return;
        }

        // Create the new merch and add it to the artist's merch array
        Merch merch = new Merch(command.getName(), command.getDescription(), command.getPrice());
        artist.getMerchandise().add(merch);

        artist.setNotificationName("New Merchandise");
        artist.setNotificationDescription("New Merchandise from " + artist.getUsername() + ".");

        // Notify the notification observers
        artist.notifyNotificationObservers();

        // Notify the observers
        PageSubject pageSubject = new PageSubject();
        pageSubject.notifyObservers(artist.getUsername());

        setMessage(this.getUsername() + " has added new merchandise successfully.");
    }

    /**
     * Sets the message indicating the result of the add merchandise operation.
     *
     * @param message The message to be set.
     */
    public void setMessage(final String message) {
        this.message = message;
    }
}

