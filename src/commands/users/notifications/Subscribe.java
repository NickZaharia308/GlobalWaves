package commands.users.notifications;

import commands.Command;
import lombok.Getter;
import lombok.Setter;
import main.Library;
import user.entities.Artist;
import user.entities.Host;
import user.entities.Users;
import user.entities.specialEntities.PageMenu;

@Getter
@Setter
public class Subscribe extends Command {
    private String message;
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

        if (!(user.getPageMenu().getCurrentPage() == PageMenu.Page.ARTISTPAGE ||
            user.getPageMenu().getCurrentPage() == PageMenu.Page.HOSTPAGE)) {
            setMessage("To subscribe you need to be on the page of an artist or host.");
            return;
        }

        // If we subscribe to an artist
        if (user.getPageMenu().getCurrentPage() == PageMenu.Page.ARTISTPAGE) {
            if (!Artist.hasObserverInSubscribers(user.getPageMenu().getPageOwnerName(), user)) {
                // Get the artist and subscribe the user to its list
                Artist artist = (Artist) user.getUser(library.getUsers(), user.getPageMenu().getPageOwnerName());
                artist.addNotificationObserver(user);

                setMessage(user.getUsername() +  " subscribed to " + user.getPageMenu().getPageOwnerName() + " successfully.");
            } else {
                // Get the artist and unsubscribe the user to its list
                Artist artist = (Artist) user.getUser(library.getUsers(), user.getPageMenu().getPageOwnerName());
                artist.removeNotificationObserver(user);

                setMessage(user.getUsername() +  " unsubscribed from " + user.getPageMenu().getPageOwnerName() + " successfully.");
            }
        }

        // If we subscribe to a host
        if (user.getPageMenu().getCurrentPage() == PageMenu.Page.HOSTPAGE) {
            if (!Host.hasUserInSubscribers(user.getPageMenu().getPageOwnerName(), user)) {
                Host host = (Host) user.getUser(library.getUsers(), user.getPageMenu().getPageOwnerName());
                host.getSubscribers().add(user);
                setMessage(user.getUsername() +  " subscribed to " + user.getPageMenu().getPageOwnerName() + " successfully.");
            } else {
                Host host = (Host) user.getUser(library.getUsers(), user.getPageMenu().getPageOwnerName());
                host.getSubscribers().remove(user);
                setMessage(user.getUsername() +  " unsubscribed from " + user.getPageMenu().getPageOwnerName() + " successfully.");
            }
        }
    }
}
