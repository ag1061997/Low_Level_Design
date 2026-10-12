package observer;

import entities.Event;

public interface PostObserver {
    void onPostEvent(Event event);
}
