package commands.users;

import commands.Command;
import lombok.Getter;
import lombok.Setter;
import main.Library;
import user.entities.Users;

@Getter
@Setter
public class adBreak extends Command {
    private String message;
    public void returnAdBreak(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());

        if (user == null) {
            setMessage("The username " + command.getUsername() + " doesn't exist.");
            return;
        }

        Status status = new Status();
        status.returnStatus(command, library);

        if (user.getMusicPlayer().isPaused()) {
            setMessage(command.getUsername() + " is not playing any music.");
            return;
        }

        setMessage("Ad inserted successfully.");
    }
}
