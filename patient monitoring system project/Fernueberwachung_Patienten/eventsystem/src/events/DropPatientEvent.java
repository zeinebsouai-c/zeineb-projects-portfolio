package events;

import interfaces.Event;

import java.util.Date;

public class DropPatientEvent implements Event {
    /**
     * This class represents an event that is triggered when a patient is dropped from a room.
     * It contains the patient ID and room ID, and the event time is set to the current time.
     */
    private Date timestamp;

    private int patientId;
    private int roomId;

    public DropPatientEvent(int patientId, int roomId) {
        this.timestamp = new Date();
        this.patientId = patientId;
        this.roomId = roomId;
    }

    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    public int getPatientId() {
        return patientId;
    }

    public int getRoomId() {
        return roomId;
    }
}
