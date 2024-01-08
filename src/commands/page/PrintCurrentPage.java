package commands.page;

import commands.Command;
import lombok.Getter;
import main.Library;
import user.entities.Users;
import user.entities.specialEntities.PageMenu;

/**
 * The PrintCurrentPage class represents a command to print the current page for a user.
 * The printed page can be: Home Page, Liked Content Page, an Artist / Host page.
 * For the Artist or Host page, the Observer pattern takes care of updating the current page
 * of the user.
 * It extends the Command class.
 */
@Getter
public class PrintCurrentPage extends Command {
    private String message;

    /**
     * Prints the current page for a user based on the provided command.
     *
     * @param command The command containing relevant details.
     * @param library The library containing user information.
     */
    public void returnPrintCurrentPage(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        Users user = new Users();
        user = user.getUser(library.getUsers(), this.getUsername());

        if (!user.isOnline()) {
            setMessage(user.getUsername() + " is offline.");
            return;
        }

        // If the page is HOMEPAGE or LIKEDCONTENTPAGE, the page has to be created
        if (user.getPageMenu().getCurrentPage() == PageMenu.Page.HOMEPAGE
                || user.getPageMenu().getCurrentPage() == PageMenu.Page.LIKEDCONTENTPAGE
                || user.getPageMenu().getPageOwnerName().equals(user.getUsername())) {
            user.getPageMenu().setPage(user, library, user.getPageMenu().getPageOwnerName());
        }

        setMessage(user.getCurrentPage());
    }

    /**
     * Sets the message indicating the current page for the user.
     *
     * @param message The message to be set.
     */
    public void setMessage(final String message) {
        this.message = message;
    }
}
