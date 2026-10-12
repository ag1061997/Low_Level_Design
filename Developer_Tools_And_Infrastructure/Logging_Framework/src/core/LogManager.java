package core;

import strategies.appender.LogAppender;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LogManager {
    private static volatile LogManager INSTANCE;
    private final Map<String, Logger> loggers;
    private final AsyncLogProcessor processor;

    private LogManager() {
        this.processor = new AsyncLogProcessor();
        this.loggers = new ConcurrentHashMap<>();
    }

    public static LogManager getInstance() {
        if (INSTANCE == null) {
            synchronized (LogManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LogManager();
                }
            }
        }
        return INSTANCE;
    }

    public Logger getLogger(String name) { return loggers.computeIfAbsent(name, Logger::new); }
    public AsyncLogProcessor getProcessor() { return processor; }

    public void shutdown() {
        processor.stop();
        loggers.values().stream()
                .flatMap(logger -> logger.getAppenders().stream())
                .distinct()
                .forEach(LogAppender::close);
        System.out.println("Logging framework shut down gracefully.");
    }
}
