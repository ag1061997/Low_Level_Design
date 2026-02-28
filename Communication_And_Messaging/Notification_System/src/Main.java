import entities.Notification;
import entities.Recipient;
import enums.NotificationType;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        NotificationService notificationService = new NotificationService(10);

        Recipient recipient1 = new Recipient("u1", "abc@gmail.com", null, "pushToken");
        Recipient recipient2 = new Recipient("u2", null, "12345", null);

        Notification welcomeEmail = new Notification.Builder(recipient1, NotificationType.EMAIL)
                .subject("Welcome")
                .message("Hello")
                .build();
        notificationService.sendNotification(welcomeEmail);

        Notification pushNotification = new Notification.Builder(recipient1, NotificationType.PUSH)
                .subject("Welcome 2")
                .message("Hello 2")
                .build();
        notificationService.sendNotification(pushNotification);

        Notification smsNotification = new Notification.Builder(recipient2, NotificationType.SMS)
                .message("Hello 3")
                .build();
        notificationService.sendNotification(smsNotification);

        Thread.sleep(1000);

        notificationService.shutDown();
        System.out.println("Shut down system");
    }
}