import core.PubSubService;
import entities.Message;
import subscriber.AlertSubscriber;
import subscriber.NewsSubscriber;
import subscriber.Subscriber;

public class Main {
    public static void main(String[] args) {
        PubSubService pubSubService = PubSubService.getInstance();

        Subscriber sportsFan1 = new NewsSubscriber("SportsFan1");
        Subscriber sportsFan2 = new NewsSubscriber("SportsFan2");
        Subscriber techie1 = new NewsSubscriber("Techie1");
        Subscriber allNewsReader = new NewsSubscriber("AllNewsReader");
        Subscriber systemAdmin = new AlertSubscriber("SystemAdmin");

        final String SPORTS_TOPIC = "SPORTS";
        final String TECH_TOPIC = "TECH";
        final String WEATHER_TOPIC = "WEATHER";

        pubSubService.createTopic(SPORTS_TOPIC);
        pubSubService.createTopic(TECH_TOPIC);
        pubSubService.createTopic(WEATHER_TOPIC);

        pubSubService.subscribe(SPORTS_TOPIC, sportsFan1);
        pubSubService.subscribe(SPORTS_TOPIC, sportsFan2);
        pubSubService.subscribe(SPORTS_TOPIC, allNewsReader);
        pubSubService.subscribe(SPORTS_TOPIC, systemAdmin);

        pubSubService.subscribe(TECH_TOPIC, techie1);
        pubSubService.subscribe(TECH_TOPIC, allNewsReader);

        System.out.println("\n--- Publishing Messages ---");

        pubSubService.publish(SPORTS_TOPIC, new Message("Team A wins the championship!"));
        pubSubService.publish(TECH_TOPIC, new Message("New AI model released."));
        pubSubService.publish(WEATHER_TOPIC, new Message("Sunny with a high of 75°F."));

        System.out.println("\n--- Unsubscribing a user and re-publishing ---");

        pubSubService.unsubscribe(SPORTS_TOPIC, sportsFan2);

        pubSubService.publish(SPORTS_TOPIC, new Message("Major player traded to Team B."));

        pubSubService.shutdown();
    }
}