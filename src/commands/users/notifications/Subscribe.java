package commands.users.notifications;

import commands.Command;
import lombok.Getter;
import lombok.Setter;
import main.Library;
import user.entities.Artist;
import user.entities.Host;
import user.entities.Users;
import user.entities.specialEntities.PageMenu;

/**
 * This class implements the Subscribe command
 * and contains the method that subscribes / unsubscribes a user to / from an artist or host.
 */
@Getter
@Setter
public class Subscribe extends Command {
    private String message;

    /**
     * This method subscribes a user to an artist or host. If the user is already subscribed,
     * it unsubscribes the user.
     * @param command the command to be processed
     * @param library the main library
     */
    public void returnSubscribe(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());

        if (user == null) {
            setMessage("The username " + command.getUsername() + " doesn't exist.");
            return;
        }

        if (!(user.getPageMenu().getCurrentPage() == PageMenu.Page.ARTISTPAGE
            || user.getPageMenu().getCurrentPage() == PageMenu.Page.HOSTPAGE)) {
            setMessage("To subscribe you need to be on the page of an artist or host.");
            return;
        }

        // If we subscribe to an artist
        if (user.getPageMenu().getCurrentPage() == PageMenu.Page.ARTISTPAGE) {
            // Get the artist
            Artist artist = (Artist) user.getUser(library.getUsers(),
                                                  user.getPageMenu().getPageOwnerName());

            if (!Artist.hasObserverInSubscribers(user.getPageMenu().getPageOwnerName(), user)) {
                //Subscribe the user to its list
                artist.addNotificationObserver(user);
                setMessage(user.getUsername() +  " subscribed to "
                        + user.getPageMenu().getPageOwnerName() + " successfully.");
            } else {
                //Unsubscribe the user to its list
                artist.removeNotificationObserver(user);
                setMessage(user.getUsername() +  " unsubscribed from "
                        + user.getPageMenu().getPageOwnerName() + " successfully.");
            }
        }

        // If we subscribe to a host
        if (user.getPageMenu().getCurrentPage() == PageMenu.Page.HOSTPAGE) {
            Host host = (Host) user.getUser(library.getUsers(),
                                            user.getPageMenu().getPageOwnerName());

            if (!Host.hasUserInSubscribers(user.getPageMenu().getPageOwnerName(), user)) {
                host.getSubscribers().add(user);
                setMessage(user.getUsername() +  " subscribed to "
                        + user.getPageMenu().getPageOwnerName() + " successfully.");
            } else {
                host.getSubscribers().remove(user);
                setMessage(user.getUsername() +  " unsubscribed from "
                        + user.getPageMenu().getPageOwnerName() + " successfully.");
            }
        }
    }
}
