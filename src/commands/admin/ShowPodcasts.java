package commands.admin;

import commands.Command;
import main.Library;
import userEntities.Host;
import userEntities.Users;
import userEntities.audio.Podcasts;

import java.util.ArrayList;

public class ShowPodcasts extends Command {
    public ArrayList<Podcasts> returnShowPodcasts(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        Users user = new Users();
        user = user.getUser(library.getUsers(), this.getUsername());

        Host host = (Host) user;
        return host.getPodcasts();
    }
}
