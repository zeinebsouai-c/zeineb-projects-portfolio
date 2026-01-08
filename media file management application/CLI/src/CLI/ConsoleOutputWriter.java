package CLI;

public class ConsoleOutputWriter implements OutputWriter{
    @Override
    public void println(String message){
        System.out.println(message);
    }
}
