package interfaces;

import java.util.Date;

public interface Event {
    /** This interface represents an event in the system.
     * It can be implemented by any class that wants to represent an event.
     * The event can be used to trigger actions within Services by the coresponding listeners.
     * The events are distributed by the {@Link other.EventDistributor EventDistributor}.
     */
    public Date getTimestamp();
}
