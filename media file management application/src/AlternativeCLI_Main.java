
import CLI.AdminCLI;
import CLI.ConsoleInputReader;
import CLI.ConsoleOutputWriter;
import EventSystem.EventDispatcher;
import EventSystem.EventType;
import domainLogic.*;
import observerPattern.CapacityObserver;

import java.util.EventListener;


public class AlternativeCLI_Main {
    public static void main(String[] args) {

        int capacity = 10000; // default capacity
        EventDispatcher eventDispatcher = new EventDispatcher();


        Admin admin = new Admin(capacity, eventDispatcher);

        // here, only the capacity observer is active

        admin.getEventDispatcher().addListener(EventType.CAPACITY_EXCEEDED, new CapacityObserver(0.9* admin.getCapacity())); // 90% limit

        ConsoleInputReader inputReader = new ConsoleInputReader();
        ConsoleOutputWriter outputWriter = new ConsoleOutputWriter();



        AdminCLI adminCLI = new AdminCLI(inputReader, outputWriter, admin);

        // disabling certain features by not setting up handlers for them
        adminCLI.setDeleteEnabled(false);
        adminCLI.setListTagsEnabled(false);




        adminCLI.start();
    }

}


