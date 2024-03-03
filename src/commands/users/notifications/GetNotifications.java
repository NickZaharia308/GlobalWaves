package commands.users.notifications;

import commands.Command;
import lombok.Getter;
import lombok.Setter;
import main.Library;
import user.entities.Users;

import java.util.LinkedList;
import java.util.Queue;

/**
 * This class implements the GetNotification command
 * and contains the method that returns the notifications
 * of a user.
 */
@Getter
@Setter
public class GetNotifications extends Command {
    private Queue<String> notifications = new LinkedList<>();

    /**
     * This method returns the notifications of a user.
     * @param command the command to be processed
     * @param library the main library
     */
    public void returnGetNotification(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());

        // Get the notifications of the user
        setNotifications(user.getNotifications());
        // Clear the notifications of the user
        user.setNotifications(new LinkedList<>());
    }
}
