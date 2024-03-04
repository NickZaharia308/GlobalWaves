package commands.users;

import commands.Command;
import lombok.Getter;
import lombok.Setter;
import main.Library;
import user.entities.Users;

import java.util.ArrayList;

/**
 * SeeMerch class is used to see the merchandise bought by a user.
 * Merchandise can be bought using the BuyMerch command.
 */
@Getter
@Setter
public class SeeMerch extends Command {
    private String message = "";
    private ArrayList<String> merch = new ArrayList<>();

    /**
     * Returns the merchandise bought by a user.
     * @param command the command to be executed
     * @param library the main library
     */
    public void returnSeeMerch(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());

        if (user == null) {
            setMessage("The username " + user.getUsername() + " doesn't exist.");
            return;
        }
        merch = user.getBoughtMerchandise();
    }

}
