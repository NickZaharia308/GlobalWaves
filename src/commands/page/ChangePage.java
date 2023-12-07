package commands.page;

import commands.Command;
import lombok.Getter;
import main.Library;
import userEntities.Users;
import userEntities.specialEntities.PageMenu;

@Getter
public class ChangePage extends Command {
    String message;

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

        if (!(command.getNextPage().equals("Home") ||
            command.getNextPage().equals("LikedContent"))) {
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

    public void setMessage(String message) {
        this.message = message;
    }
}
