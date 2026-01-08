package exeptions;

import Log.Logger;

public class ExceptionHelper {

    public static void handleException(Exception e){
        System.err.println("ERROR: " + e.getMessage() + "\nStacktrace: " + e.getStackTrace());
        Logger.logException("ExceptionHelper", e);
    }
}
