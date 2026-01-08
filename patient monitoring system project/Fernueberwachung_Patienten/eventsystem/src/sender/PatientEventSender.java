package sender;

import coreLogic.Patient;
import events.AlertEvent;
import events.UpdatePatientEvent;
import exeptions.ExceptionHelper;
import other.EventDistributor;

public class PatientEventSender {

    /**
     * This class is responsible for sending events from the patientService.
     */
    private final EventDistributor eventDistributor = new EventDistributor();

    public void sendAlertEvent(AlertEvent alertEvent) {
        eventDistributor.distributeEvent(alertEvent);
    }

    public void sendUpdatePatientEvent(Patient patient) {
        try{
            if (patient == null) {
                throw new IllegalArgumentException("Patient cannot be null");
            }
            UpdatePatientEvent event = new UpdatePatientEvent(patient);

            eventDistributor.distributeEvent(event);
        } catch (IllegalArgumentException iae) {
            ExceptionHelper.handleException(iae);
        }
    }
}
