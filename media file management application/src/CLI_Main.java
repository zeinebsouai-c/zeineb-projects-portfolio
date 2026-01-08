import CLI.AdminCLI;
import CLI.ConsoleInputReader;
import CLI.ConsoleOutputWriter;
import domainLogic.*;
import EventSystem.*;


public class CLI_Main {
    public static void main(String[] args){

        int capacity = 10000; // default capacity
        EventDispatcher eventDispatcher = new EventDispatcher();

        if (args.length > 0) {
            try {
                capacity = Integer.parseInt(args[0]);
                System.out.println("Capacity set to: " + capacity);
            }
            catch (NumberFormatException e) {
                System.out.println("Invalid capacity provided. Using default capacity: " + capacity);
            }
        } else {
            System.out.println("No capacity provided. Using default capacity: " + capacity);
        }

        ConsoleInputReader consoleInputReader = new ConsoleInputReader();
        ConsoleOutputWriter consoleOutputWriter = new ConsoleOutputWriter();
        Admin admin = new Admin(capacity,eventDispatcher);
        AdminCLI cli = new AdminCLI(consoleInputReader, consoleOutputWriter,admin);
        cli.start();
    }
}
