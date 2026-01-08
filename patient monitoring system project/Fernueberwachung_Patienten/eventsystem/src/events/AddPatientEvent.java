package events;

import interfaces.Event;
import util.FormateUtil;

import java.util.Date;

public class AddPatientEvent implements Event {
    /**
     * This class represents an event that is triggered when a new patient is added.
     * This event will cause a new patient to be added to a room in the hospital.
     */
    private Date timestamp;
    private int patientId;
    private int roomId;

    public AddPatientEvent(int patientId, int roomId) {
        this.timestamp = FormateUtil.getEuropeanDateFormat(new Date());
        this.patientId = patientId;
        this.roomId = roomId;
    }
    public int getPatientId() {
        return patientId;
    }

    public int getRoomId() {
        return roomId;
    }

    @Override
    public Date getTimestamp() {
        return timestamp;
    }
}
