package commands.page.command;

import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.Pair;
import user.entities.specialEntities.PageMenu;

import java.util.*;

interface PageCommandInterface {
    void execute(Pair<PageMenu.Page, String> pagePair);
    boolean undo();
    boolean redo();
}

@Getter
@Setter
public class PageCommand implements PageCommandInterface {
    private Stack<Pair<PageMenu.Page, String>> prevStack = new Stack<>();
    private Stack<Pair<PageMenu.Page, String>> nextStack = new Stack<>();

    public void execute(Pair<PageMenu.Page, String> pagePair) {
        nextStack.push(pagePair);
    }

    public boolean redo() {
        if (!prevStack.isEmpty()) {
            Pair<PageMenu.Page, String> pagePair = prevStack.pop();
            nextStack.push(pagePair);
            return true;
        }
        return false;
    }

    public boolean undo() {
        if (!nextStack.isEmpty()) {
            Pair<PageMenu.Page, String> pagePair = nextStack.pop();
            prevStack.push(pagePair);
            return true;
        }
        return false;
    }
}
