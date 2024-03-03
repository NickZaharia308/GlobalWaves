package commands.users.notifications;

/**
 * This interface is used to implement the Observer pattern.
 * The subject is represented as an artist or a host and notifies the users when there is a new
 * event, album, merchandise, or other notifications.
 */
public interface NotificationSubject {
    void addNotificationObserver(NotificationObserver notificationObserver);
    void removeNotificationObserver(NotificationObserver notificationObserver);
    void notifyNotificationObservers();

}
