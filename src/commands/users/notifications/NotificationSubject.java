package commands.users.notifications;

/**
 * This interface is used to implement the Observer pattern.
 * The subject is represented as an artist or a host and notifies the users when there is a new
 * event, album, merchandise, or other notifications.
 */
public interface NotificationSubject {

    /**
     * Adds a new observer to the list of subscribers.
     * @param notificationObserver the observer to be added
     */
    void addNotificationObserver(NotificationObserver notificationObserver);

    /**
     * Removes an observer from the list of subscribers.
     * @param notificationObserver the observer to be removed
     */
    void removeNotificationObserver(NotificationObserver notificationObserver);

    /**
     * Notifies all the observers when there is a new event, album, merchandise, or other
     * notifications.
     */
    void notifyNotificationObservers();

}
