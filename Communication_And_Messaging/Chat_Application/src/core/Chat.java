package core;

import entities.Message;
import entities.User;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Chat {
    protected final String id;
    protected final List<User> members;
    protected final List<Message> messages;

    public Chat() {
        this.id = UUID.randomUUID().toString();
        this.members = new CopyOnWriteArrayList<>(); // Thread-safe for reads
        this.messages = new CopyOnWriteArrayList<>();
    }

    public String getId() { return id; }
    public List<User> getMembers() { return List.copyOf(members); }
    public List<Message> getMessages() { return List.copyOf(messages); }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public abstract String getName(User perspectiveUser);
}
