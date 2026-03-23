package Strategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TokenBucketRateLimiter implements RateLimiter {
    private final int capacity;
    private final int refillRatePerSecond;
    private final Map<String, TokenBucket> userBuckets = new ConcurrentHashMap<>();

    public TokenBucketRateLimiter(int capacity, int refillRatePerSecond) {
        this.capacity = capacity;
        this.refillRatePerSecond = refillRatePerSecond;
    }

    @Override
    public boolean allowRequest(String userId) {
        long currentTime = System.currentTimeMillis();
        userBuckets.putIfAbsent(userId, new TokenBucketRateLimiter.TokenBucket(capacity, refillRatePerSecond, currentTime));

        TokenBucket tokenBucket = userBuckets.get(userId);

        synchronized (tokenBucket) {
            tokenBucket.refill(currentTime);
            if (tokenBucket.tokens > 0) {
                tokenBucket.tokens--;
                return true;
            } else {
                return false;
            }
        }
    }

    private static class TokenBucket {
        int tokens;
        final int capacity;
        final int refillRatePerSecond;
        long lastRefillTimestamp;

        public TokenBucket(int capacity, int refillRatePerSecond, long currentTimeMillis) {
            this.tokens = capacity;
            this.capacity = capacity;
            this.refillRatePerSecond = refillRatePerSecond;
            this.lastRefillTimestamp = currentTimeMillis;
        }

        public void refill(long currentTime) {
            long elapsedTime = currentTime - lastRefillTimestamp;
            int tokensToAdd = (int) ((elapsedTime / 1000.0) * refillRatePerSecond);

            if (tokensToAdd > 0) {
                tokens = Math.min(capacity, tokens + tokensToAdd);
                lastRefillTimestamp = currentTime;
            }
        }
    }
}
