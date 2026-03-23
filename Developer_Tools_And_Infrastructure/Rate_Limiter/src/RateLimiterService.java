import Strategy.RateLimiter;

public class RateLimiterService {
    private static RateLimiterService instance;
    private RateLimiter rateLimiter;

    private RateLimiterService() {}

    public static synchronized RateLimiterService getInstance() {
        if (instance == null) {
            instance = new RateLimiterService();
        }
        return instance;
    }

    public void setRateLimiter(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    public void handleRequest(String userId) {
        if (rateLimiter.allowRequest(userId)) {
            System.out.println("Allowed For " + userId);
        } else {
            System.out.println("Not Allowed For " + userId);
        }
    }
}
