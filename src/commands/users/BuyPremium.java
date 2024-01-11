package commands.users;

import commands.Command;
import lombok.Getter;
import lombok.Setter;
import main.Library;
import user.entities.Users;

@Getter
@Setter
public class BuyPremium extends Command {
    private String message;
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
