package commands.page.command;

import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.Pair;
import user.entities.specialEntities.PageMenu;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

interface PageCommandInterface {
    void execute(Pair<PageMenu.Page, String> pagePair);
    boolean undo();
    boolean redo();
}

@Getter
@Setter
public class PageCommand implements PageCommandInterface {
    private Queue<Pair<PageMenu.Page, String>> prevQueue = new LinkedList<>();
    private Queue<Pair<PageMenu.Page, String>> nextQueue = new LinkedList<>();

    public void execute(Pair<PageMenu.Page, String> pagePair) {
        nextQueue.offer(pagePair);
    }

    public boolean redo() {
        if (!prevQueue.isEmpty()) {
            Pair<PageMenu.Page, String> pagePair = prevQueue.poll();
            nextQueue.offer(pagePair);
            return true;
        }
        return false;
    }

    public boolean undo() {
        if (!nextQueue.isEmpty()) {
            Pair<PageMenu.Page, String> pagePair = nextQueue.poll();
            prevQueue.offer(pagePair);
            return true;
        }
        return false;
    }
}
