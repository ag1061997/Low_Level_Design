package strategies.formatter;

import entities.LogMessage;

public class JsonFormatter implements LogFormatter {
    @Override
    public String format(LogMessage m) {
        return String.format(
                "{\"timestamp\":\"%s\",\"level\":\"%s\",\"logger\":\"%s\",\"thread\":\"%s\",\"message\":\"%s\"}\n",
                m.getTimestamp(), m.getLevel(), m.getLoggerName(),
                m.getThreadName(), escape(m.getMessage()));
    }

    private String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
