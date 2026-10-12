package core;

import entities.Message;
import entities.Topic;
import subscriber.Subscriber;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PubSubService {
    private static volatile PubSubService INSTANCE;
    private final Map<String, Topic> topicRegistry;

    private PubSubService() {
        this.topicRegistry = new ConcurrentHashMap<>();
    }

    public static PubSubService getInstance() {
        if (INSTANCE == null) {
            synchronized (PubSubService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PubSubService();
                }
            }
        }
        return INSTANCE;
    }

    public void createTopic(String topicName) {
        topicRegistry.putIfAbsent(topicName, new Topic(topicName));
        System.out.println("Topic " + topicName + " created");
    }

    public void subscribe(String topicName, Subscriber subscriber) {
        Topic topic = topicRegistry.get(topicName);
        if (topic == null) throw new IllegalArgumentException("Topic not found: " + topicName);
        topic.addSubscriber(subscriber);
        System.out.println("Subscriber '" + subscriber.getId() + "' subscribed to topic: " + topicName);
    }

    public void unsubscribe(String topicName, Subscriber subscriber) {
        Topic topic = topicRegistry.get(topicName);
        if (topic != null) topic.removeSubscriber(subscriber);
        System.out.println("Subscriber '" + subscriber.getId() + "' unsubscribed from topic: " + topicName);
    }

    public void publish(String topicName, Message message) {
        System.out.println("Publishing message to topic: " + topicName);
        Topic topic = topicRegistry.get(topicName);
        if (topic == null) throw new IllegalArgumentException("Topic not found: " + topicName);
        topic.broadcast(message);
    }

    public void shutdown() {
        System.out.println("PubSubService shutting down...");
        System.out.println("PubSubService shutdown complete.");
    }
}
