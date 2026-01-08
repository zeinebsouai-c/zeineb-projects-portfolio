package events;

import hospital.Hospital;
import interfaces.Event;

import java.util.Date;

public class PersistenceAppSateEvent implements Event {
    private Date timeStamp;

    private Hospital hospital;

    public PersistenceAppSateEvent(Hospital hospital) {
        this.hospital = hospital;
    }

    public Hospital getHospital() {
        return hospital;
    }

    @Override
    public Date getTimestamp() {
        return timeStamp;
    }
}
