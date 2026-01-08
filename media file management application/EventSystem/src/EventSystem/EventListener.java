package EventSystem;

public interface EventListener {
    void handleEvent(EventType eventType, Object data);
}
