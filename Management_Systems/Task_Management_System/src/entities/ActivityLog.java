package entities;

import java.time.LocalDateTime;

public class ActivityLog {
    private final String description;
    private final LocalDateTime timestamp;

    public ActivityLog(String description) {
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }

    public String getDescription() { return description; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
