package commands.page.observer;

import main.Library;

/**
 * The Observer interface represents an object that can observe changes in the system's state.
 * Classes implementing this interface must provide the update method,
 * which is called when a change occurs.
 * The Users class implements the Observer interface so that any normal user that searched a page
 * of an artist or a host, gets an update when the subject (host or artist) modifies its page.
 */
public interface PageObserver {
    /**
     * Updates the observer with the latest state of the system.
     *
     * @param library The Library instance representing the current state of the system.
     */
    void update(Library library);
}
