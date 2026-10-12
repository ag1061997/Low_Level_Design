package observer;

import entities.ShortenedURL;
import enums.EventType;

public interface Observer {
    void update(EventType type, ShortenedURL url);
}
