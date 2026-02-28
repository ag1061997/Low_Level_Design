package strategy;

import entities.Notification;

public class EmailGateway implements NotificationGateway {
    @Override
    public void send(Notification notification) throws Exception {
        String email = notification.getRecipient().getEmail()
                .orElseThrow(() -> new IllegalArgumentException("Email id is not available"));
        System.out.println("Sending email: " + email);
    }
}
