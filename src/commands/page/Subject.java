package commands.page;

import lombok.Getter;
import main.Library;

import java.util.ArrayList;
import java.util.Map;

@Getter
public class Subject {
    private final Library library = Library.getInstance();
    private Map<String, ArrayList<Observer>> observersMap = library.getObserversMap();

    // Register an observer for a specific artist or host
    public void addObserver(String subject, Observer observer) {
        observersMap.computeIfAbsent(subject, k -> new ArrayList<>()).add(observer);
        // Update the Observer Map from the library
        library.setObserversMap(observersMap);
    }

    // Unregister an observer for a specific artist or host
    public void removeObserver(String subject, Observer observer) {
        observersMap.getOrDefault(subject, new ArrayList<>()).remove(observer);
        // Update the Observer Map from the library
        library.setObserversMap(observersMap);
    }

    // Notify all observers for a specific artist or host
    public void notifyObservers(String subject) {
        for (Observer observer : observersMap.getOrDefault(subject, new ArrayList<>())) {
            observer.update(library);
        }
    }
}
