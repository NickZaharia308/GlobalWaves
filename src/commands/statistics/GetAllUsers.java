package commands.statistics;

import commands.Command;
import main.Library;
import userEntities.Users;

import java.util.ArrayList;

public class GetAllUsers extends Command {

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
