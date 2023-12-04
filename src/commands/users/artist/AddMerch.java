package commands.users.artist;

import commands.Command;
import lombok.Getter;
import main.Library;
import userEntities.Artist;
import userEntities.Users;
import userEntities.specialEntities.Event;
import userEntities.specialEntities.Merch;

import java.util.ArrayList;

@Getter
public class AddMerch extends Command {
    private String message;

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
        setMessage(this.getUsername() + " has added new merchandise successfully.");
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
