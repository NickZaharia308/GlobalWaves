package commands.users.notifications;

public interface NotificationObserver {
    public void update(final NotificationSubject notificationSubject);
}
