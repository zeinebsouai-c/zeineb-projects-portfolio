package other;

import Log.Logger;
import events.UpdatePatientEvent;
import exeptions.ExceptionHelper;
import interfaces.Event;
import interfaces.EventListener;
import listener.DBEventListener;
import listener.FrontEndEventListener;
import listener.PatientEventListener;
import listener.VitalDataEventListener;
import util.FormateUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class EventDistributor {
    /** This class is responsible for distributing events to the appropriate listeners.
     * It maintains a list of listeners and notifies them when an event occurs.
     * The events are coming from any {@Link interfaces.SendService} and are distributed based on the event-class.
     */
    private static ArrayList<EventListener> listeners = new ArrayList<>(Arrays.asList(
        new DBEventListener(),
        new FrontEndEventListener(),
        new PatientEventListener(),
        new VitalDataEventListener()
    ));
    /**
     * Distributes the given event based on its class
     * @param event
     */
    public static void distributeEvent(Event event){
        if(event == null){
            ExceptionHelper.handleException(new IllegalArgumentException("EventDistributor: Event cannot be null"));
            return;
        }
        //Important for the logger
        boolean isEventHandled = false;

        switch(event.getClass().getName().toLowerCase()){
            //Update Status
            case "events.updatepstatusevent" -> {

                for(EventListener listener : listeners){
                    if(listener instanceof VitalDataEventListener){
                        listener.handleEvent(event);
                    }
                }
                isEventHandled = true;
            }
            //Add Patient
            case "events.addpatientevent" -> {

                for(EventListener listener : listeners){
                    if(listener instanceof PatientEventListener){
                        listener.handleEvent(event);
                    }
                }
                isEventHandled = true;
            }
            //Return from AddPatient
            case "events.updatepatientevent" -> {
                UpdatePatientEvent updatePatientEvent = (UpdatePatientEvent) event;
                for(EventListener listener : listeners){
                    if(listener instanceof FrontEndEventListener){
                        listener.handleEvent(updatePatientEvent);
                    }
                }
                isEventHandled = true; //set the debug point here to see the patient object
            }


            //Alert Event
            case "events.alertevent" -> {

                for(EventListener listener : listeners){
                    if(listener instanceof FrontEndEventListener){
                        listener.handleEvent(event);
                    }
                }
                isEventHandled = true;
            }

            default -> {
                ExceptionHelper.handleException(new IllegalStateException(
                    "EventDistributor: Unknown event type: " + event.getClass().getName()
                ));
            }
        }

        if(isEventHandled){
            Logger.logEvent(event);
        }else{
            Logger.logException(FormateUtil.getEuropeanDateFormat(new Date()) + "EventDiistributor: ", new IllegalStateException(
                "EventDistributor: Event was not handled by any listener: " + event.getClass().getName()
            ));
        }
    }
}
