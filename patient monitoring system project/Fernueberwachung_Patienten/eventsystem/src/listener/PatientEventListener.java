package listener;

import Log.Logger;
import events.AddPatientEvent;
import exeptions.ExceptionHelper;
import interfaces.Event;
import interfaces.EventListener;
import patientservices.PatientService;
import sender.PatientEventSender;

public class PatientEventListener implements EventListener {
  /**
   * This class listens to events related to the patient
   */
  private PatientService patientService = new PatientService();
  private PatientEventSender patientEventSender = new PatientEventSender();

  @Override
  public void handleEvent(Event event) {
    try {

      String eventClass = event.getClass().getSimpleName();
      Logger.logEvent(  event);

      if (eventClass == null || eventClass.isEmpty()) {
        throw new IllegalArgumentException("Event class name cannot be null or empty");
      }

      switch (eventClass) {
        case "AddPatientEvent" -> {
          AddPatientEvent castedEvent = (AddPatientEvent) event;
          //TODO: Logic in Hospital to get new PatientId and RoomId;
          patientEventSender.sendUpdatePatientEvent(patientService.createNewPatient(castedEvent.getPatientId(), castedEvent.getRoomId()));

        }

        default -> {
          throw new IllegalArgumentException("Unknown event type: " + eventClass);
        }
      }

    } catch (IllegalArgumentException iae) {
      ExceptionHelper.handleException(iae);
    }
  }
}
