package core;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BalanceSheet {
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, Double>> balances;

    public BalanceSheet() {
        this.balances = new ConcurrentHashMap<>();
    }

    public synchronized void updateBalance(String fromUserId, String toUserId, double amount) {
        balances.computeIfAbsent(fromUserId, k -> new ConcurrentHashMap<>()).merge(toUserId, amount, Double::sum);
        balances.computeIfAbsent(toUserId, k -> new ConcurrentHashMap<>()).merge(fromUserId, -amount, Double::sum);
    }

    public synchronized void settleUp(String fromUserId, String toUserId, double amount) {
        updateBalance(fromUserId, toUserId, -amount);
    }

    public double getBalance(String userId1, String userId2) {
        ConcurrentHashMap<String, Double> userBalances = balances.get(userId1);
        if (userBalances == null) return 0.0;
        return userBalances.getOrDefault(userId2, 0.0);
    }

    public Map<String, Double> getBalancesForUser(String userId) {
        ConcurrentHashMap<String, Double> userBalances = balances.get(userId);
        if (userBalances == null) return new HashMap<>();
        return new HashMap<>(userBalances);
    }
}
