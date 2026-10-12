import Strategy.FixedWindowRateLimiter;
import Strategy.TokenBucketRateLimiter;
import core.RateLimiterService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        String userId = "user123";

        System.out.println("Fixed Window Demo");
        runFixedWindowDemo(userId);

        System.out.println("Token Bucket Demo");
        runTokenBucketDemo(userId);
    }

    private static void runFixedWindowDemo(String userId) {
        int maxRequests = 5;
        int windowSize = 10;

        FixedWindowRateLimiter fixedWindowRateLimiter = new FixedWindowRateLimiter(maxRequests, windowSize);
        RateLimiterService rateLimiterService = RateLimiterService.getInstance();
        rateLimiterService.setRateLimiter(fixedWindowRateLimiter);

        for (int i = 0; i < 10; i++) {
            rateLimiterService.handleRequest(userId);
        }
    }

    private static void runTokenBucketDemo(String userId) {
        int capacity = 5;
        int refillRate = 1;

        TokenBucketRateLimiter tokenBucketRateLimiter = new TokenBucketRateLimiter(capacity, refillRate);
        RateLimiterService rateLimiterService = RateLimiterService.getInstance();
        rateLimiterService.setRateLimiter(tokenBucketRateLimiter);

        for (int i = 0; i < 10; i++) {
            rateLimiterService.handleRequest(userId);
        }
    }
}