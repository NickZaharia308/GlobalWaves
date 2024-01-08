package commands.statistics;

import commands.Command;
import main.Library;
import user.entities.Users;

import java.util.ArrayList;

/**
 * The GetAllUsers class represents a command to retrieve all users from the library.
 * It extends the Command class and provides a method to categorize users based on their type.
 */
public class GetAllUsers extends Command {

    /**
     * Retrieves all users from the library and categorizes them into three lists based on
     * their user type: normal users, artists, and hosts. Combines the lists into a single
     * list and returns it.
     *
     * @param command The command containing information about the operation.
     * @param library The library containing all user entities.
     * @return An ArrayList containing all users categorized by their user type.
     */
    public ArrayList<Users> returnGetAllUsers(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setTimestamp(command.getTimestamp());

        ArrayList<Users> normalUsers = new ArrayList<>();
        ArrayList<Users> artists = new ArrayList<>();
        ArrayList<Users> hosts = new ArrayList<>();
        ArrayList<Users> allUsers = library.getUsers();

        for (Users user : allUsers) {
            if (user.getUserType() == Users.UserType.NORMAL) {
                normalUsers.add(user);
            } else if (user.getUserType() == Users.UserType.ARTIST) {
                artists.add(user);
            } else {
                hosts.add(user);
            }
        }

        // Combining all three lists into a single list
        ArrayList<Users> combinedList = new ArrayList<>();
        combinedList.addAll(normalUsers);
        combinedList.addAll(artists);
        combinedList.addAll(hosts);

        return combinedList;
    }
}
