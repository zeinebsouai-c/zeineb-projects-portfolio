package events;

import coreLogic.Alert;
import coreLogic.Patient;
import interfaces.Event;
import util.FormateUtil;

import java.util.Date;

public class AlertEvent implements Event {

    /*
     * AlertEvent represents an event that occurs when an alert is triggered for a patient.
     */

    private Patient patient;
    private Alert alert;
    private Date timestamp;

    public AlertEvent(Patient patient, Alert alert) {
        this.patient = patient;
        this.alert = alert;
        this.timestamp = FormateUtil.getEuropeanDateFormat(new Date()); // Set the timestamp to the current time
    }

    public Patient getPatient() {
        return patient;
    }

    public Alert getAlert() {
        return alert;
    }

    @Override
    public Date getTimestamp() {
        return timestamp;
    }
}
