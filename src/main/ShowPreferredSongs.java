package main;

import java.util.ArrayList;

public class ShowPreferredSongs extends Command {
    private ArrayList<Songs> likedSongs;

    public void returnShowPreferredSongs (Command command, Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        // Finding the user
        ArrayList<Users> users = library.getUsers();
        Users user = null;
        for (Users user1 : users) {
            if (user1.getUsername().equals(command.getUsername())) {
                user = user1;
                break;
            }
        }

        setLikedSongs(user.getLikedSongs());
    }
    public ArrayList<Songs> getLikedSongs() {
        return likedSongs;
    }

    public void setLikedSongs(ArrayList<Songs> likedSongs) {
        this.likedSongs = likedSongs;
    }
}
