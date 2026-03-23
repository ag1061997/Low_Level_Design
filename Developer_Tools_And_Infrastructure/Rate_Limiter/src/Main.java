import Strategy.FixedWindowRateLimiter;
import Strategy.TokenBucketRateLimiter;

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

        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 10; i++) {
            executor.submit(() -> rateLimiterService.handleRequest(userId));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        executor.shutdown();
    }

    private static void runTokenBucketDemo(String userId) {
        int capacity = 5;
        int refillRate = 1;

        TokenBucketRateLimiter tokenBucketRateLimiter = new TokenBucketRateLimiter(capacity, refillRate);
        RateLimiterService rateLimiterService = RateLimiterService.getInstance();
        rateLimiterService.setRateLimiter(tokenBucketRateLimiter);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        for (int i = 0; i < 10; i++) {
            executor.submit(() -> rateLimiterService.handleRequest(userId));
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        executor.shutdown();
    }
}