package commands.statistics;

import commands.Command;
import main.Library;
import userEntities.Users;

import java.util.ArrayList;

public class GetOnlineUsers extends Command {
    public ArrayList<Users> returnGetOnlineUsers(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setTimestamp(command.getTimestamp());

        ArrayList<Users> onlineUsers = new ArrayList<>();
        ArrayList<Users> allUsers = library.getUsers();

        for (Users user : allUsers) {
            if (user.isOnline()) {
                onlineUsers.add(user);
            }
        }
        return onlineUsers;
    }
}
