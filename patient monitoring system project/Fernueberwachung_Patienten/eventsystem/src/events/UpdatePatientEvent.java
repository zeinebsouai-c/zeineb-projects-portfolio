package events;

import coreLogic.Patient;
import interfaces.Event;
import util.FormateUtil;

import java.util.Date;

public class UpdatePatientEvent implements Event {
    /**
     * This class represents an event that is triggered when a patient is updated.
     * It is also used to add a new patient to the hospital system.
     * This event will cause the patient's information to be updated in the hospital system.
     */
    private final Patient patient;
    private final Date timestamp;

    public UpdatePatientEvent(Patient patient) {
       this.patient = patient;
       this.timestamp = FormateUtil.getEuropeanDateFormat(new Date());
    }

    public Patient getPatient() {
        return patient;
    }

    @Override
    public Date getTimestamp() {
        return timestamp;
    }
}
