package strategy;

import entities.Notification;

public class SmsGateway implements NotificationGateway {
    @Override
    public void send(Notification notification) throws Exception {
        String phoneNumber = notification.getRecipient().getPhoneNumber()
                .orElseThrow(() -> new IllegalArgumentException("Phone number is not available"));
        System.out.println("Sending sms: " + phoneNumber);
    }
}
