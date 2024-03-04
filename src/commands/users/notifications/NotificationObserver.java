package commands.users.notifications;

/**
 * This interface is used to implement the Observer pattern.
 * The observer is implemented by the Users class.
 * Normal users can subscribe to artists and hosts and receive notifications.
 */
public interface NotificationObserver {

    /**
     * Updates the observer when there is a new event, album, merchandise, or other notifications.
     * @param notificationSubject the subject that notifies the observer
     */
    void update(NotificationSubject notificationSubject);
}
