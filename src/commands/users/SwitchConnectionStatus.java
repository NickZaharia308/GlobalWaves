package commands.users;

import commands.Command;
import lombok.Getter;
import main.Library;
import user.entities.Users;

/**
 * The {@code SwitchConnectionStatus} class represents a command to switch the online
 * status of a normal user.
 * It extends the {@link Command} class and is used to process and execute commands
 * related to changing the connection status of a normal user.
 * The class includes methods to validate the input command, toggle the online status of the user,
 * and update the play timestamp if the user is switching to online status.
 */
@Getter
public class SwitchConnectionStatus extends Command {
    private String message;

    /**
     * Processes the input command to switch the online status of a normal user
     *
     * @param command The command containing information about the user and the
     * operation to be performed.
     * @param library The main library containing information about users and entities.
     */
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

        if (user.getUserType() != Users.UserType.NORMAL) {
            setMessage(super.getUsername() + " is not a normal user.");
            return;
        }

        if (user.isOnline()) {
            Status status = new Status();
            status.returnStatus(command, library);
            user.setOnline(false);
        } else {
            user.setOnline(true);

            if (user.getMusicPlayer() != null) {
                user.getMusicPlayer().setPlayTimestamp(command.getTimestamp());
            }
        }

        setMessage(super.getUsername() + " has changed status successfully.");
    }

    /**
     * Sets the message indicating the result of the switch connection status operation.
     *
     * @param message The message to be set.
     */
    public void setMessage(final String message) {
        this.message = message;
    }
}

