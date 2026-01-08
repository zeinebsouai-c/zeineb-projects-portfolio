package listener;

import coreLogic.Alert;
import coreLogic.Patient;
import coreLogic.PatientStatus;
import events.AlertEvent;
import events.UpdatePStatusEvent;
import interfaces.Event;
import interfaces.EventListener;
import patientservices.PatientService;
import randomValue.RandomValueGeneration;
import sender.PatientEventSender;

public class VitalDataEventListener implements EventListener {
    /**
     * This class listens to events related to vital data updates.
     */

    private final RandomValueGeneration randomValueGeneration = new RandomValueGeneration();
    private final PatientEventSender patientEventSender = new PatientEventSender();
    private final PatientService patientService = new PatientService();

    @Override
    public void handleEvent(Event event) {

        if(event != null){

            UpdatePStatusEvent updateEvent = (UpdatePStatusEvent) event;
            if(updateEvent.getPatient() != null) {

                Patient patient = updateEvent.getPatient();
                patient = patientService.updatePatientVitalData(patient);
                patient = patientService.evaluatePatientStatus(patient);

                if(patient.getPatientStatusLevel().equals(PatientStatus.Level.CRITICAL)){
                    patientEventSender.sendUpdatePatientEvent(patient);
                    patientEventSender.sendAlertEvent(new AlertEvent(
                            patient,
                            new Alert(
                                    patient.getPatientId(),
                                    null,
                                    patient.getPatientStatusLevel(),
                                    patient.getRoomId() )
                    ));
                }else{
                    patientEventSender.sendUpdatePatientEvent(patient);
                }
            }

        }

    }
}
