package observer;

import entities.Order;

public interface OrderObserver {
    void onUpdate(Order order);
}
