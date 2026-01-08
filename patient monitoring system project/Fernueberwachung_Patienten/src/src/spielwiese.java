import events.UpdatePStatusEvent;
import frontend.FrontEndService;
import hospital.Hospital;
import randomValue.RandomValueGeneration;
import sender.FrontEndEventSender;

public class spielwiese {

    public static void main(String[] args) throws InterruptedException {
        //Ignore this
        RandomValueGeneration rvg = new RandomValueGeneration();

        //This should happen on start of the system

        FrontEndService frontEndService = new FrontEndService();
        FrontEndEventSender f = new FrontEndEventSender();;
        /**
         * Set a breakpoint on the events.updatepatientevent case in the distributeEvent on the "isEventHandled" boolean.
         * Than evaluate the "Patient, and you will see that it now has updated VitalData and a newly set Status
         */
        Hospital h = frontEndService.getHospital();

        UpdatePStatusEvent event1 = new UpdatePStatusEvent(h.getPatientList().get(1));
        f.sendUpdatePStatusEvent(event1);

        h = frontEndService.getHospital();
        while(true){
            System.out.println(h.getPatientList().get(1).getPatientVitals().getHeartRate());
            Thread.sleep(1000);
        }

    }
}
