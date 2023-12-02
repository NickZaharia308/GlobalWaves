package commands.admin;

import commands.Command;
import lombok.Getter;
import main.Library;
import userEntities.Artist;
import userEntities.Host;
import userEntities.Users;

import java.util.ArrayList;

@Getter
public class AddUser extends Command {
    private String message;

    public void returnAddUser(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setTimestamp(command.getTimestamp());
        super.setUsername(command.getUsername());

        ArrayList<Users> allUsers= library.getUsers();

        // If the user already exists
        Users user = new Users();
        user = user.getUser(allUsers, getUsername());
        if (user != null) {
            setMessage("The username " + getUsername() + " is already taken.");
            return;
        }


        // Add the user based on its type
        if (command.getType().toUpperCase().equals(Users.UserType.ARTIST.toString())) {
            Artist newArtist = new Artist(getUsername(), command.getAge(), command.getCity());
            newArtist.setUserType(Users.UserType.ARTIST);
            allUsers.add(newArtist);
        } else if (command.getType().toUpperCase().equals(Users.UserType.HOST.toString())) {
            Host newHost = new Host(getUsername(), command.getAge(), command.getCity());
            newHost.setUserType(Users.UserType.HOST);
            allUsers.add(newHost);
        } else {
            Users newUser = new Users(getUsername(), command.getAge(), command.getCity());
            allUsers.add(newUser);
        }

        setMessage("The username " + getUsername() + " has been added successfully.");
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
