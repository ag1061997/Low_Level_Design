package Strategy;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SlidingWindowLogRateLimiter implements RateLimiter {
    private int maxRequests;
    private final long windowSize;
    private final Map<String, Deque<Long>> userLogs = new ConcurrentHashMap<>();

    public SlidingWindowLogRateLimiter(int maxRequests, long windowSize) {
        this.maxRequests = maxRequests;
        this.windowSize = windowSize * 1000;
    }

    public void updateLimit(int newLimit) {
        maxRequests = newLimit;
    }

    @Override
    public boolean allowRequest(String userId) {
        long now = System.currentTimeMillis();
        Deque<Long> log = userLogs.computeIfAbsent(userId, k -> new ArrayDeque<>());

        synchronized (log) {
            while (!log.isEmpty() && log.peekFirst() <= now - windowSize) {
                log.pollFirst();
            }
            if (log.size() < maxRequests) {
                log.addLast(now);
                return true;
            }
            return false;
        }
    }
}
