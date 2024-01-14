package commands.users.notifications;

import commands.Command;
import lombok.Getter;
import lombok.Setter;
import main.Library;
import user.entities.Users;

import java.util.LinkedList;
import java.util.Queue;

@Getter
@Setter
public class GetNotification extends Command {
    private Queue<String> notifications = new LinkedList<>();

    public void returnGetNotification(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());
        setNotifications(user.getNotifications());
        user.setNotifications(new LinkedList<>());
    }
}
