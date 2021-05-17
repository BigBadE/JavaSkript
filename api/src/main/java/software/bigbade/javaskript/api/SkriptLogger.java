package software.bigbade.javaskript.api;

import java.util.logging.Logger;

public class SkriptLogger {
    private static Logger logger;

    public static void setLogger(Logger logger) {
        if(SkriptLogger.logger != null) {
            throw new IllegalStateException("Cannot set already-set logger!");
        }

        SkriptLogger.logger = logger;
    }

    public static Logger getLogger() {
        return logger;
    }
}
