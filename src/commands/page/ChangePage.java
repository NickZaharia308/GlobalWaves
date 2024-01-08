package commands.page;

import commands.Command;
import lombok.Getter;
import main.Library;
import user.entities.Users;
import user.entities.specialEntities.PageMenu;

/**
 * The ChangePage class represents a command to change the current page for a user.
 * The user can choose from: Home page and Liked content page.
 * It extends the Command class.
 */
@Getter
public class ChangePage extends Command {
    private String message;

    /**
     * Changes the current page for a user based on the provided command.
     *
     * @param command The command containing relevant details.
     * @param library The library containing user information.
     */
    public void returnChangePage(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setTimestamp(command.getTimestamp());
        super.setUsername(command.getUsername());

        Users user = new Users();
        user = user.getUser(library.getUsers(), this.getUsername());

        if (user == null) {
            setMessage("The username " + this.getUsername() + " doesn't exist.");
            return;
        }

        if (!(command.getNextPage().equals("Home")
                || command.getNextPage().equals("LikedContent"))) {
            setMessage(this.getUsername() + " is trying to access a non-existent page.");
            return;
        }

        if (command.getNextPage().equals("Home")) {
            user.getPageMenu().setCurrentPage(PageMenu.Page.HOMEPAGE);
        } else if (command.getNextPage().equals("LikedContent")) {
            user.getPageMenu().setCurrentPage(PageMenu.Page.LIKEDCONTENTPAGE);
            user.getPageMenu().setPageOwnerName("");
        }
        setMessage(this.getUsername() + " accessed " + command.getNextPage() + " successfully.");
    }

    /**
     * Sets the message indicating the result of the page change.
     *
     * @param message The message to be set.
     */
    public void setMessage(final String message) {
        this.message = message;
    }
}
