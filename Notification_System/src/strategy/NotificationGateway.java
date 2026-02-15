package strategy;

import entities.Notification;

public interface NotificationGateway {
    void send(Notification notification) throws Exception;
}
