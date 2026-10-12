package services;

public interface NotificationService {
    void notifyCustomer(String orderId, String code, String lockerAddress, long expirationTime);
}
