package commands.admin;

import commands.Command;
import main.Library;
import user.entities.Host;
import user.entities.Users;
import user.entities.audio.files.Podcasts;

import java.util.ArrayList;

/**
 * The ShowPodcasts class represents a command to retrieve
 * a list of podcasts associated with a specific host.
 */
public class ShowPodcasts extends Command {

    /**
     * Retrieves and returns the list of podcasts of a specific host.
     *
     * @param command The command containing information about the requested operation.
     * @param library The main library containing user and podcast data.
     * @return An ArrayList of Podcasts associated with the specified user.
     */
    public ArrayList<Podcasts> returnShowPodcasts(final Command command, final Library library) {
        // Set command-related information
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        Users user = new Users();
        user = user.getUser(library.getUsers(), this.getUsername());

        Host host = (Host) user;
        return host.getPodcasts();
    }
}
