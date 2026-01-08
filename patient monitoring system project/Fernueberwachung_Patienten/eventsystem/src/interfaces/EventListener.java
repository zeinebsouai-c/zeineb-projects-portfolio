package interfaces;

public interface EventListener {
    /** This interface represents a listener for events in the system.
     * It can be implemented by any class that wants to listen for events.
     * The listener will be notified when an event occurs, allowing it to take appropriate action.
     * The events are distributed by the {@link other.EventDistributor EventDistributor}.
     *
     * @param event the event that occurred
     */
    public void handleEvent(Event event);
}
