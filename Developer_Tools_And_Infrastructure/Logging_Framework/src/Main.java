import core.LogManager;
import core.Logger;
import enums.LogLevel;
import strategies.appender.ConsoleAppender;

public class Main {
    public static void main(String[] args) {
        LogManager logManager = LogManager.getInstance();

        Logger logger = logManager.getLogger("com.example.Main");
        logger.setLevel(LogLevel.INFO);
        logger.addAppender(new ConsoleAppender());
        logger.info("Application starting up.");
        logger.debug("This is a debug message, it should NOT appear.");
        logger.warn("This is a warning message.");

        Logger serviceLogger = logManager.getLogger("com.example.UserService");
        serviceLogger.setLevel(LogLevel.DEBUG);
        serviceLogger.addAppender(new ConsoleAppender());
        serviceLogger.info("User service starting.");
        serviceLogger.debug("Detailed debug output for the user service.");

        logger.setLevel(LogLevel.DEBUG);
        logger.debug("This debug message should now be visible.");

        try {
            Thread.sleep(500);
            logManager.shutdown();
        } catch (Exception e) {
            System.out.println("Caught exception");
        }
    }
}