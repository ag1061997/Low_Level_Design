package strategy;

import entities.Notification;

public class PushGateway implements NotificationGateway {
    @Override
    public void send(Notification notification) throws Exception {
        String pushToken = notification.getRecipient().getPushToken()
                .orElseThrow(() -> new IllegalArgumentException("Push token is not available"));
        System.out.println("Sending push notification: " + pushToken);
    }
}
