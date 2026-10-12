package entities;

import enums.EventType;

public class Event {
    private final EventType type;
    private final User actor;
    private final Post targetPost;
    private final boolean reversal;

    public Event(EventType type, User actor, Post targetPost) {
        this(type, actor, targetPost, false);
    }

    public Event(EventType type, User actor, Post targetPost, boolean reversal) {
        this.type = type;
        this.actor = actor;
        this.targetPost = targetPost;
        this.reversal = reversal;
    }

    public EventType getType() { return type; }
    public User getActor() { return actor; }
    public Post getTargetPost() { return targetPost; }
    public boolean isReversal() { return reversal; }
}
