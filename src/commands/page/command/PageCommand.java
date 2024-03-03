package commands.page.command;

import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.Pair;
import user.entities.specialEntities.PageMenu;

import java.util.Stack;

/**
 * The PageCommandInterface represents an interface implemented by the PageCommand class.
 * It is used to execute, undo, and redo commands that change the current page of a user.
 * It uses pairs with the page and the page owner's name to keep track of the pages.
 */
interface PageCommandInterface {
    void execute(Pair<PageMenu.Page, String> pagePair);
    boolean undo();
    boolean redo();
}

@Getter
@Setter
/**
 * The PageCommand class represents a command that allows users to navigate
 * to a page they have been previously on.
 */
public class PageCommand implements PageCommandInterface {
    private Stack<Pair<PageMenu.Page, String>> prevStack = new Stack<>();
    private Stack<Pair<PageMenu.Page, String>> nextStack = new Stack<>();

    /**
     * Navigates the user to the given page.
     *
     * @param pagePair The page and the page owner's name to be navigated to.
     */
    public void execute(final Pair<PageMenu.Page, String> pagePair) {
        nextStack.push(pagePair);
    }

    /**
     * Navigates the user to the previous page in the stack of pages they have been on.
     *
     * @return True if the user can go a page back, false otherwise.
     */
    public boolean redo() {
        if (!prevStack.isEmpty()) {
            Pair<PageMenu.Page, String> pagePair = prevStack.pop();
            nextStack.push(pagePair);
            return true;
        }
        return false;
    }

    /**
     * Navigates the user to the next page in the stack of pages they have been on.
     *
     * @return True if the user can go a page forward, false otherwise.
     */
    public boolean undo() {
        if (!nextStack.isEmpty()) {
            Pair<PageMenu.Page, String> pagePair = nextStack.pop();
            prevStack.push(pagePair);
            return true;
        }
        return false;
    }
}
