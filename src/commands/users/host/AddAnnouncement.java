package commands.users.host;

import commands.Command;
import commands.page.Subject;
import lombok.Getter;
import main.Library;
import userEntities.Host;
import userEntities.Users;
import userEntities.specialEntities.Announcement;

import java.util.ArrayList;

@Getter
public class AddAnnouncement extends Command {
    private String message;

    public void returnAddAnnouncement(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        Users user = new Users();
        user = user.getUser(library.getUsers(), this.getUsername());

        if (user == null) {
            setMessage("The username " + this.getUsername() + " doesn't exist.");
            return;
        }

        if (user.getUserType() != Users.UserType.HOST) {
            setMessage(this.getUsername() + " is not a host.");
            return;
        }

        Host host = (Host) user;

        ArrayList<Announcement> announcements = host.getAnnouncements();

        for (Announcement announcement : announcements) {
            if (announcement.getName().equals(command.getName())) {
                setMessage(this.getUsername() + " has already added an announcement with this name.");
                return;
            }
        }

        // Create the new announcement and add it to the host's announcement array
        Announcement announcement = new Announcement(command.getName(), command.getDescription());
        host.getAnnouncements().add(announcement);

        // Notify the observers
        Subject subject = new Subject();
        subject.notifyObservers(host.getUsername());

        setMessage(this.getUsername() + " has successfully added new announcement.");
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
