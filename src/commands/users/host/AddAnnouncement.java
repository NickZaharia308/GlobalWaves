package commands.users.host;

import commands.Command;
import commands.page.observer.PageSubject;
import lombok.Getter;
import main.Library;
import user.entities.Host;
import user.entities.Users;
import user.entities.specialEntities.Announcement;

import java.util.ArrayList;

/**
 * The {@code AddAnnouncement} class represents a command to add an announcement
 * by a host.
 * It extends the {@link Command} class and is used to process and execute commands related
 * to adding announcements to the host's collection. The class includes methods to validate
 * the input command, check if an announcement with the same name already exists, and add the
 * new announcement to the host's collection.
 */
@Getter
public class AddAnnouncement extends Command {
    private String message;

    /**
     * Processes the input command to add an announcement by a host user and notifies observers.
     *
     * @param command The command containing information about the user and
     *                the operation to be performed.
     * @param library The main library containing information about users and entities.
     */
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
                setMessage(this.getUsername()
                        + " has already added an announcement with this name.");
                return;
            }
        }

        // Create the new announcement and add it to the host's announcement array
        Announcement announcement = new Announcement(command.getName(), command.getDescription());
        host.getAnnouncements().add(announcement);

        // Notify the observers
        PageSubject pageSubject = new PageSubject();
        pageSubject.notifyObservers(host.getUsername());

        setMessage(this.getUsername() + " has successfully added new announcement.");
    }

    /**
     * Sets the message indicating the result of the add announcement operation.
     *
     * @param message The message to be set.
     */
    public void setMessage(final String message) {
        this.message = message;
    }
}

