package commands.users.playlists;

import commands.Command;
import lombok.Getter;
import lombok.Setter;
import main.Library;
import user.entities.Users;

import java.util.ArrayList;

@Getter
@Setter
public class SeeMerch extends Command {
    private String message = "";
    private ArrayList<String> merch = new ArrayList<>();

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
