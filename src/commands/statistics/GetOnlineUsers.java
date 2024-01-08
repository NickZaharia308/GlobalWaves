package commands.statistics;

import commands.Command;
import main.Library;
import user.entities.Users;

import java.util.ArrayList;

/**
 * The GetOnlineUsers class represents a command to retrieve a list of online normal
 * users from the library.
 * It extends the Command class.
 */
public class GetOnlineUsers extends Command {

    /**
     * Retrieves a list of online normal users from the library.
     *
     * @param command The command containing relevant details.
     * @param library The library containing user information.
     * @return An ArrayList of Users representing online normal users.
     */
    public ArrayList<Users> returnGetOnlineUsers(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setTimestamp(command.getTimestamp());

        ArrayList<Users> onlineUsers = new ArrayList<>();
        ArrayList<Users> allUsers = library.getUsers();

        for (Users user : allUsers) {
            if (user.isOnline() && user.getUserType() == Users.UserType.NORMAL) {
                onlineUsers.add(user);
            }
        }
        return onlineUsers;
    }
}
