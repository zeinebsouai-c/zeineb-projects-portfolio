package sender;

import events.AddPatientEvent;
import events.UpdatePStatusEvent;
import Log.Logger;
import other.EventDistributor;

/**
 * The FrontEndEventSender is responsible for sending events from the frontend
 * to the EventDistributor. It supports sending AddPatientEvent and UpdatePStatusEvent.
 */

public class FrontEndEventSender {

  private final EventDistributor eventDistributor = new EventDistributor();

  /**
   * Sends an AddPatientEvent to the EventDistributor and logs the event.
   *
   * @param event the AddPatientEvent to be sent
   */

  public void sendAddPatientEvent(AddPatientEvent event) {
    Logger.logEvent(event);
    eventDistributor.distributeEvent(event);
  }

  /**
   * Sends an UpdatePStatusEvent to the EventDistributor and logs the event.
   *
   * @param event the UpdatePStatusEvent to be sent
   */

  public void sendUpdatePStatusEvent(UpdatePStatusEvent event) {
    Logger.logEvent(event);
    eventDistributor.distributeEvent(event);
  }
}
