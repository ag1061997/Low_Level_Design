package factory;

import enums.NotificationType;
import strategy.EmailGateway;
import strategy.NotificationGateway;
import strategy.PushGateway;
import strategy.SmsGateway;

import java.util.HashMap;
import java.util.Map;

public class NotificationFactory {
    private static final Map<NotificationType, NotificationGateway> gatewayMap = new HashMap<>();

    public static NotificationGateway createGateway(NotificationType notificationType) {
        if(gatewayMap.containsKey(notificationType)) {
            return gatewayMap.get(notificationType);
        }

        NotificationGateway gateway = null;

        switch(notificationType) {
            case EMAIL:
                gateway = new EmailGateway();
                break;
            case SMS:
                gateway = new SmsGateway();
                break;
            case PUSH:
                gateway = new PushGateway();
                break;
        }

        gatewayMap.put(notificationType, gateway);
        return gateway;
    }
}
