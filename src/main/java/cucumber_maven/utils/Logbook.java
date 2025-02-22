package cucumber_maven.utils;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Logbook {

    private static final String LOG_FILE = "model_logbook.txt"; 

    public static void log(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            String timestamp = LocalDateTime.now().toString();
            writer.write(timestamp + " - " + message + "\n");
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }
    
 // Example method to log the start of a step
    public static void logStepStart(String stepName) {
        log("Starting step: " + stepName);
    }

    // Example method to log the end of a step
    public static void logStepEnd(String stepName) {
        log("Completed step: " + stepName);
    }

    // Method to log errors or exceptions
    public static void logError(String errorMessage) {
        log("ERROR: " + errorMessage);
    }
}
