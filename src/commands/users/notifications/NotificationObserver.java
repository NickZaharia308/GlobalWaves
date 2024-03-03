package commands.users.notifications;

/**
 * This interface is used to implement the Observer pattern.
 * The observer is implemented by the Users class.
 * Normal users can subscribe to artists and hosts and receive notifications.
 */
public interface NotificationObserver {
    void update(final NotificationSubject notificationSubject);
}
