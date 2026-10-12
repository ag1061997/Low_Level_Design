package observers;

import entities.Order;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Subject {
    private final List<OrderObserver> observers = new CopyOnWriteArrayList<>();

    public void addObserver(OrderObserver observer) { observers.add(observer); }
    public void removeObserver(OrderObserver observer) { observers.remove(observer); }
    public void notifyObservers(Order order) {
        for (OrderObserver observer : observers) {
            observer.update(order);
        }
    }
}
