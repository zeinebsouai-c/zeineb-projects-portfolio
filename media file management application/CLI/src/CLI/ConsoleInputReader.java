package CLI;

import java.util.Scanner;

public class ConsoleInputReader implements InputReader {
    private Scanner scanner = new Scanner(System.in);

    @Override
    public String read(String type) {
        switch (type) {
            case "command":
            case "title":
            case "address":
            case "filename":
            case "display command":
            case "input":
                return scanner.nextLine();
            case "samplingRate":
            case "duration":
                int value = scanner.nextInt();
                scanner.nextLine();
                return String.valueOf(value);
            default:
                throw new IllegalStateException("Invalid input type.");
        }
    }
}
