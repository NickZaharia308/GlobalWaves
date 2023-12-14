package commands.page;

import commands.Command;
import lombok.Getter;
import main.Library;
import userEntities.Users;
import userEntities.specialEntities.PageMenu;

@Getter
public class PrintCurrentPage extends Command {
    private String message;

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
        if (user.getPageMenu().getCurrentPage() == PageMenu.Page.HOMEPAGE ||
            user.getPageMenu().getCurrentPage() == PageMenu.Page.LIKEDCONTENTPAGE ||
            user.getPageMenu().getPageOwnerName().equals(user.getUsername())) {
            user.getPageMenu().setPage(user, library, user.getPageMenu().getPageOwnerName());
        }

        setMessage(user.getCurrentPage());
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
