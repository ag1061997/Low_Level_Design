package decorator;

import entities.Notification;
import strategy.NotificationGateway;

public class RetryableGateway implements NotificationGateway {
    private final NotificationGateway wrappedGateway;
    private final int maxRetries;
    private final long retryDelay;

    public RetryableGateway(NotificationGateway wrappedGateway, int maxRetries, long retryDelay) {
        this.wrappedGateway = wrappedGateway;
        this.maxRetries = maxRetries;
        this.retryDelay = retryDelay;
    }

    @Override
    public void send(Notification notification) throws Exception {
        int attempt = 0;
        while(attempt < maxRetries) {
            try {
                wrappedGateway.send(notification);
                return;
            } catch (Exception e) {
                attempt++;
                System.out.println("Error: Attempt " + attempt + " fail to send notification " + notification.getId() + " Retrying..");
                if(attempt >= maxRetries) {
                    System.out.println(e.getMessage());
                    throw new Exception("Failed to send notification");
                }
                Thread.sleep(retryDelay);
            }
        }
    }
}
