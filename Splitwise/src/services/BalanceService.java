package services;

import models.Balance;
import models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BalanceService {

    // Map<userId1, Map<userId2, amount>> where userId1 owes userId2 the amount
    private Map<String, Map<String, Double>> balances;
    private static BalanceService instance;

    private BalanceService() {
        this.balances = new HashMap<>();
    }

    public static BalanceService getInstance() {
        if (instance == null) {
            instance = new BalanceService();
        }
        return instance;
    }

    public void updateBalance(String userId1, String userId2, double amount) {
        // userId1 owes userId2 the amount
        balances.putIfAbsent(userId1, new HashMap<>());
        balances.putIfAbsent(userId2, new HashMap<>());

        Map<String, Double> user1Balances = balances.get(userId1);
        Map<String, Double> user2Balances = balances.get(userId2);

        double currentBalance = user1Balances.getOrDefault(userId2, 0.0);
        double reverseBalance = user2Balances.getOrDefault(userId1, 0.0);

        if (reverseBalance > 0) {
            // userId2 already owes userId1, adjust the balance
            if (reverseBalance >= amount) {
                user2Balances.put(userId1, reverseBalance - amount);
            } else {
                user2Balances.put(userId1, 0.0);
                user1Balances.put(userId2, amount - reverseBalance);
            }
        } else {
            user1Balances.put(userId2, currentBalance + amount);
        }
    }

    public void settleBalance(String userId1, String userId2, double amount) {
        // userId1 is settling amount with userId2
        balances.putIfAbsent(userId1, new HashMap<>());

        Map<String, Double> user1Balances = balances.get(userId1);
        double currentBalance = user1Balances.getOrDefault(userId2, 0.0);

        if (currentBalance >= amount) {
            user1Balances.put(userId2, currentBalance - amount);
            System.out.println("Settlement successful! " + userId1 + " paid " + amount + " to " + userId2);
        } else {
            System.out.println("Invalid settlement amount. " + userId1 + " only owes " + currentBalance + " to " + userId2);
        }
    }

    public List<Balance> getUserBalances(String userId) {
        List<Balance> userBalances = new ArrayList<>();

        // Get all people this user owes
        if (balances.containsKey(userId)) {
            for (Map.Entry<String, Double> entry : balances.get(userId).entrySet()) {
                if (entry.getValue() > 0.01) { // Ignore very small amounts
                    userBalances.add(new Balance(userId, entry.getKey(), entry.getValue()));
                }
            }
        }

        // Get all people who owe this user
        for (Map.Entry<String, Map<String, Double>> entry : balances.entrySet()) {
            String otherUserId = entry.getKey();
            if (!otherUserId.equals(userId)) {
                double amount = entry.getValue().getOrDefault(userId, 0.0);
                if (amount > 0.01) {
                    userBalances.add(new Balance(otherUserId, userId, amount));
                }
            }
        }

        return userBalances;
    }

    public void showUserBalances(String userId) {
        UserService userService = UserService.getInstance();
        User user = userService.getUser(userId);

        System.out.println("\n=== Balances for " + user.getName() + " ===");

        List<Balance> userBalances = getUserBalances(userId);

        if (userBalances.isEmpty()) {
            System.out.println("No pending balances.");
            return;
        }

        for (Balance balance : userBalances) {
            User user1 = userService.getUser(balance.getUserId1());
            User user2 = userService.getUser(balance.getUserId2());

            if (balance.getUserId1().equals(userId)) {
                System.out.printf("%s owes %s: %.2f\n",
                        user1.getName(), user2.getName(), balance.getAmount());
            } else {
                System.out.printf("%s owes %s: %.2f\n",
                        user1.getName(), user2.getName(), balance.getAmount());
            }
        }
    }

    public void showAllBalances() {
        UserService userService = UserService.getInstance();
        System.out.println("\n=== All Balances ===");

        boolean hasBalances = false;
        for (Map.Entry<String, Map<String, Double>> entry : balances.entrySet()) {
            String userId1 = entry.getKey();
            User user1 = userService.getUser(userId1);

            for (Map.Entry<String, Double> balanceEntry : entry.getValue().entrySet()) {
                if (balanceEntry.getValue() > 0.01) {
                    String userId2 = balanceEntry.getKey();
                    User user2 = userService.getUser(userId2);
                    System.out.printf("%s owes %s: %.2f\n",
                            user1.getName(), user2.getName(), balanceEntry.getValue());
                    hasBalances = true;
                }
            }
        }

        if (!hasBalances) {
            System.out.println("No pending balances.");
        }
    }
}
