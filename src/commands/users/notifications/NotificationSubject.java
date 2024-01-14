package commands.users.notifications;

import java.util.ArrayList;

public interface NotificationSubject {
    public void addNotificationObserver(NotificationObserver notificationObserver);
    public void removeNotificationObserver(NotificationObserver notificationObserver);
    public void notifyNotificationObservers();

}
