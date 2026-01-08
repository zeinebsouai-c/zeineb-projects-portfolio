package EventSystem;

import java.util.*;

public class EventDispatcher {

    private final Map<EventType, Set<EventListener>> listeners = new HashMap<>(); //set instead of list to prevent duplicates

    public void addListener(EventType eventType, EventListener listener) {
        listeners.computeIfAbsent(eventType, k -> new HashSet<>()).add(listener);
    }

    public void dispatchEvent(EventType eventType, Object data) {
        Set<EventListener> eventListeners = listeners.get(eventType);
        if (eventListeners != null){
            for (EventListener listener: eventListeners){
                listener.handleEvent(eventType, data);
            }
        }
    }
}
