package entities;

import core.Chat;

import java.util.UUID;

public class User {
    private final String id;
    private final String name;

    public User(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }

    public void onMessageReceived(Message message, Chat chatContext) {
        System.out.printf("[Notification for %s in chat '%s'] %s: %s\n",
                this.getName(), chatContext.getName(this), message.getSender().getName(), message.getContent());
    }
}
