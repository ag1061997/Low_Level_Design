package Strategy;

public interface RateLimiter {
    boolean allowRequest(String userId);
}
