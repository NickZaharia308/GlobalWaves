package commands.users;

import commands.Command;
import commands.users.Status;
import lombok.Getter;
import main.Library;
import userEntities.Users;

@Getter
public class SwitchConnectionStatus extends Command {
    private String message;

    public void returnSwitchConnectionStatus(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());

        if (user == null) {
            setMessage("The username " + super.getUsername() + " doesn't exist.");
            return;
        }

        if (user.isOnline()) {
            if (user.getMusicPlayer() != null) {
                Status status = new Status();
                status.returnStatus(command, library);
            }

            user.setOnline(false);
        } else {
            user.setOnline(true);

            if (user.getMusicPlayer() != null) {
                user.getMusicPlayer().setPlayTimestamp(command.getTimestamp());
            }
        }

        setMessage(super.getUsername() + " has changed status successfully.");
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
