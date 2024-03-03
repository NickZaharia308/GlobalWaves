package commands.page.command;

import commands.Command;
import lombok.Getter;
import lombok.Setter;
import main.Library;
import org.antlr.v4.runtime.misc.Pair;
import user.entities.Users;
import user.entities.specialEntities.PageMenu;

/**
 * The NextPage class represents a command that allows users to navigate
 * to a page they have been previously on and went backwards from it.
 */
@Getter
@Setter
public class NextPage extends Command {
    private String message;

    /**
     * Navigates the user to the next page in the stack of pages they have been on.
     *
     * @param command The command containing information about the user.
     * @param library The main library containing user data.
     */
    public void returnNextPage(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setTimestamp(command.getTimestamp());
        super.setUsername(command.getUsername());

        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());

        // If the user can go a page forward
        if (user.getPageCommand().redo()) {
            Pair<PageMenu.Page, String> pageMap = user.getPageCommand().getNextStack().peek();
            user.getPageMenu().setCurrentPage(pageMap.a);
            user.getPageMenu().setPageOwnerName(pageMap.b);
            setMessage("The user " + user.getUsername()
                    + " has navigated successfully to the next page.");
            return;
        }
        setMessage("There are no pages left to go forward.");
    }
}
