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
public class RemoveAnnouncement extends Command {
    private String message;

    public void returnRemoveAnnouncement(final Command command, final Library library) {
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
        Announcement announcementToDelete = null;
        for (Announcement announcement : announcements) {
            if (announcement.getName().equals(command.getName())) {
                announcementToDelete = announcement;
                break;
            }
        }

        // If there is no announcement with the given name
        if (announcementToDelete == null) {
            setMessage(this.getUsername() + " has no announcement with the given name.");
            return;
        }

        // Delete announcement
        host.getAnnouncements().remove(announcementToDelete);

        // Notify the observers
        Subject subject = new Subject();
        subject.notifyObservers(host.getUsername());

        setMessage(this.getUsername() + " has successfully deleted the announcement.");
    }


    public void setMessage(String message) {
        this.message = message;
    }
}
