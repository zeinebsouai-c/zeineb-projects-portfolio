package Log;

import interfaces.Event;
import util.FormateUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Logger {

    private static final String LOG_FILE = "application.log";

    private static void writeLog(String source, String message, Date timestamp) {

       FormateUtil.getEuropeanDateFormat(timestamp);
        String logEntry = String.format("[%s] [%s] %s%n", timestamp, source, message);

        try (FileWriter fw = new FileWriter(LOG_FILE, true)) {
            fw.write(logEntry);
        } catch (IOException e) {
            // Optionally handle logging errors
            e.printStackTrace();
        }
    }

    public static void logEvent(Event event) {
        writeLog( event.getClass().getSimpleName(), event.toString(), event.getTimestamp());
    }

    public static void logMessage(String source, String message) {
        writeLog(source, message, new Date());
    }

    public static void logException(String source, Exception ex) {
        writeLog(source, "Exception: " + ex.getMessage() + "\nStacktrace: " + Arrays.toString(ex.getStackTrace()), new Date());
    }
}

