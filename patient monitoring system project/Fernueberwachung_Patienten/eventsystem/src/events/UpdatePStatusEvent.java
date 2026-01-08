package events;

import coreLogic.Patient;
import hospital.Room;
import util.FormateUtil;

import java.util.Date;

public class UpdatePStatusEvent implements interfaces.Event {
    /**
     * This class represents an event that is triggered when vital data is updated.
     * As Data it uses a given {@Link hospital.Room Room} object and has to be distributed into the {@Link services.patientservices.StatusService StatusService}.
     */
    private Date timestamp;
    private Patient patient;
    private Room room;

    public UpdatePStatusEvent(Room room) {
        this.timestamp = new Date();

        this.room = room;
    }

    public UpdatePStatusEvent(Patient patient){
        this.patient = patient;
        this.timestamp = FormateUtil.getEuropeanDateFormat(new Date());
    }


    @Override
    public Date getTimestamp() {
        return timestamp;
    }
    public Room getRoom() {
        return room;
    }

    public Patient getPatient() {
        return patient;
    }
}
