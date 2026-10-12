package observers;

import entities.Order;

public interface OrderObserver {
    void update(Order order);
}
