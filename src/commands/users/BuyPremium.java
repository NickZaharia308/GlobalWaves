package commands.users;

import commands.Command;
import lombok.Getter;
import lombok.Setter;
import main.Library;
import user.entities.Users;

/**
 * BuyPremium class has the purpose to buy a premium subscription for a user. (if the user is not
 * already premium)
 */
@Getter
@Setter
public class BuyPremium extends Command {
    private String message;

    /**
     * Buys a premium subscription for a user.
     * @param command the command to be executed
     * @param library the main library
     */
    public void returnBuyPremium(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());

        if (user == null) {
            setMessage("The username " + command.getUsername() + " doesn't exist.");
            return;
        }

        if (user.isPremium()) {
            setMessage(command.getUsername() + " is already a premium user.");
            return;
        }

        user.setPremium(true);
        setMessage(command.getUsername() + " bought the subscription successfully.");
    }
}
