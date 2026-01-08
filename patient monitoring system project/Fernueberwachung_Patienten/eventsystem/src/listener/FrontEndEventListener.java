package listener;

import events.AlertEvent;
import events.UpdatePatientEvent;
import exeptions.ExceptionHelper;
import frontend.FrontEndService;
import hospital.HospitalService;
import interfaces.Event;
import interfaces.EventListener;

/**
 * The FrontEndEventListener listens for events relevant to the frontend
 * and prints debug messages based on the event type.
 * <p>
 * Handles AddPatientEvent, AlertEvent, and UpdatePStatusEvent
 * to support frontend updates and logging.
 */
public class FrontEndEventListener implements EventListener {


    /**
     * Handles incoming events and processes them based on their type.
     *
     * @param event the event received by the listener
     */
    public FrontEndEventListener() {
    }

    public FrontEndEventListener(HospitalService hospitalService) {

    }

    @Override
    public void handleEvent(Event event) {
        if(event == null){
            ExceptionHelper.handleException(new IllegalArgumentException(getClass().getName() + ": Event is null!"));
        }
        switch(event.getClass().getName().toLowerCase()){

            case "events.updatepatientevent" -> {

                UpdatePatientEvent updatePatientEvent = (UpdatePatientEvent) event;
                FrontEndService.addPatientToList(updatePatientEvent.getPatient());
            }

            case "events.alertevent" -> {

                AlertEvent alertEvent = (AlertEvent) event;
                FrontEndService.handleAlert(alertEvent);
            }

            default -> {
                ExceptionHelper.handleException(new IllegalArgumentException(getClass().getName() + " the event was not correctly distributed!"));
            }
        }

    }
}
