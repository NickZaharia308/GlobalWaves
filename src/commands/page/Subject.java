package commands.page;

import lombok.Getter;
import main.Library;

import java.util.ArrayList;
import java.util.Map;

/**
 * The Subject class represents the subject in the observer pattern. It is responsible for
 * registering, unregistering, and notifying observers (Normal Users) for an update about
 * artist or host.
 * It contains a reference to the Library instance.
 */
@Getter
public class Subject {
    private final Library library = Library.getInstance();
    /**
     * The Map structure is used to store more one-to-many relations. The String represents
     *      the name of the host or artist, while the Observer is a list which contains all normal
     *      users which selected the artist or the host page.
     */
    private Map<String, ArrayList<Observer>> observersMap = library.getObserversMap();

    /**
     * Registers an observer for a specific artist or host.
     *
     * @param subject  The subject (artist or host) for which the observer is registered.
     * @param observer The observer to be registered.
     */
    public void addObserver(final String subject, final Observer observer) {
        observersMap.computeIfAbsent(subject, k -> new ArrayList<>()).add(observer);
        // Update the Observer Map from the library
        library.setObserversMap(observersMap);
    }

    /**
     * Unregisters an observer for a specific artist or host.
     *
     * @param subject  The subject (artist or host) for which the observer is unregistered.
     * @param observer The observer to be unregistered.
     */
    public void removeObserver(final String subject, final Observer observer) {
        observersMap.getOrDefault(subject, new ArrayList<>()).remove(observer);
        // Update the Observer Map from the library
        library.setObserversMap(observersMap);
    }

    /**
     * Notifies all observers for a specific artist or host.
     *
     * @param subject The subject (artist or host) for which observers are notified.
     */
    public void notifyObservers(final String subject) {
        for (Observer observer : observersMap.getOrDefault(subject, new ArrayList<>())) {
            observer.update(library);
        }
    }
}
