package entities;

import subscriber.Subscriber;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class Topic {
    private final String name;
    private final Set<Subscriber> subscribers;

    public Topic(String name) {
        this.name = name;
        this.subscribers = new CopyOnWriteArraySet<>();
    }

    public String getName() { return name; }

    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void removeSubscriber(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public void broadcast(Message message) {
        for (Subscriber subscriber : subscribers) {
            try {
                subscriber.onMessage(message);
            } catch (Exception e) {
                System.err.println("Error delivering message to subscriber " + subscriber.getId() + ": " + e.getMessage());
            }
        }
    }
}
