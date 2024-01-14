package commands.users.host;

import commands.Command;
import commands.page.PageSubject;
import lombok.Getter;
import main.Library;
import user.entities.Host;
import user.entities.Users;
import user.entities.specialEntities.Announcement;

import java.util.ArrayList;

/**
 * The {@code RemoveAnnouncement} class represents a command to remove an announcement
 * by a host.
 * It extends the {@link Command} class and is used to process and execute commands
 * related to removing announcements from the host's collection. The class includes methods
 * to validate the input command, check if the announcement to be deleted exists, and remove
 * the announcement from the host's collection.
 */
@Getter
public class RemoveAnnouncement extends Command {
    private String message;

    /**
     * Processes the input command to remove an announcement by a host user and notifies observers.
     *
     * @param command The command containing information about the user and the operation
     *                to be performed.
     * @param library The main library containing information about users and entities.
     */
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
        PageSubject pageSubject = new PageSubject();
        pageSubject.notifyObservers(host.getUsername());

        setMessage(this.getUsername() + " has successfully deleted the announcement.");
    }

    /**
     * Sets the message indicating the result of the remove announcement operation.
     *
     * @param message The message to be set.
     */
    public void setMessage(final String message) {
        this.message = message;
    }
}

