package Strategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FixedWindowRateLimiter implements RateLimiter {
    private final int maxRequests;
    private final long windowSize;
    private final Map<String, UserRequestInfo> userRequestMap = new ConcurrentHashMap<>();

    public FixedWindowRateLimiter(int maxRequests, long windowSize) {
        this.maxRequests = maxRequests;
        this.windowSize = windowSize * 1000;
    }

    @Override
    public boolean allowRequest(String userId) {
        long currentTime = System.currentTimeMillis();
        userRequestMap.putIfAbsent(userId, new UserRequestInfo(currentTime));

        UserRequestInfo userRequestInfo = userRequestMap.get(userId);

        synchronized (userRequestInfo) {
            if (currentTime - userRequestInfo.windowStart >= windowSize) {
                userRequestInfo.reset(currentTime);
            }

            if (userRequestInfo.requestCount.get() < maxRequests) {
                userRequestInfo.requestCount.incrementAndGet();
                return true;
            } else {
                return false;
            }
        }
    }

    private static class UserRequestInfo {
        long windowStart;
        AtomicInteger requestCount;

        UserRequestInfo(long startTime) {
            this.windowStart = startTime;
            this.requestCount = new AtomicInteger(0);
        }

        void reset(long newStart) {
            this.windowStart = newStart;
            this.requestCount.set(0);
        }
    }
}
