package commands.page.command;

import commands.Command;
import lombok.Getter;
import lombok.Setter;
import main.Library;
import org.antlr.v4.runtime.misc.Pair;
import user.entities.Users;
import user.entities.specialEntities.PageMenu;

@Getter
@Setter
public class NextPage extends Command {
    String message;

    public void returnNextPage(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setTimestamp(command.getTimestamp());
        super.setUsername(command.getUsername());

        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());


        if (user.getPageCommand().redo()) {
            Pair<PageMenu.Page, String> pageMap = user.getPageCommand().getNextStack().peek();
            user.getPageMenu().setCurrentPage(pageMap.a);
            user.getPageMenu().setPageOwnerName(pageMap.b);
            setMessage("The user " + user.getUsername() + " has navigated successfully to the next page.");
            return;
        }
        setMessage("There are no pages left to go forward.");
    }
}
