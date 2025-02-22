package cucumber_maven.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logbook {

    private static final Logger logger = LoggerFactory.getLogger(Logbook.class);

    public static void log(String message) {
        logger.info(message);
    }

    public static void logStepStart(String stepName) {
        logger.info("Starting step: " + stepName);
    }

    public static void logStepEnd(String stepName) {
        logger.info("Completed step: " + stepName);
    }

    public static void logError(String errorMessage, Throwable throwable) {
        logger.error("ERROR: " + errorMessage, throwable);
    }
}
