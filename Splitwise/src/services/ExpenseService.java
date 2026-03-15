package services;

import enums.ExpenseType;
import models.Expense;
import models.Group;
import models.User;
import splitstrategies.EqualSplit;
import splitstrategies.ExactSplit;
import splitstrategies.PercentSplit;
import splitstrategies.Split;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ExpenseService {
    private List<Expense> expenses;
    private static ExpenseService instance;
    private int expenseCounter = 0;

    private ExpenseService() {
        this.expenses = new ArrayList<>();
    }

    public static ExpenseService getInstance() {
        if (instance == null) {
            instance = new ExpenseService();
        }
        return instance;
    }

    public void addExpense(String description, double totalAmount, String paidBy, 
                          List<Split> splits, ExpenseType expenseType, String groupId) {
        
        // Process splits based on type
        processSplits(splits, totalAmount);
        
        String expenseId = "EXP" + (++expenseCounter);
        Expense expense = new Expense(expenseId, description, totalAmount, 
                                     paidBy, splits, expenseType, groupId);
        
        // Validate expense
        if (!expense.validate()) {
            System.out.println("Invalid expense! Split amounts don't match total amount.");
            return;
        }
        
        expenses.add(expense);
        
        // Update balances
        BalanceService balanceService = BalanceService.getInstance();
        for (Split split : splits) {
            if (!split.getUserId().equals(paidBy)) {
                // This user owes the payer
                balanceService.updateBalance(split.getUserId(), paidBy, split.getAmount());
            }
        }
        
        // If group expense, add to group
        if (expenseType == ExpenseType.GROUP && groupId != null) {
            GroupService groupService = GroupService.getInstance();
            Group group = groupService.getGroup(groupId);
            if (group != null) {
                group.addExpense(expenseId);
            }
        }
        
        System.out.println("Expense added successfully: " + description + " for Rs." + totalAmount);
    }

    private void processSplits(List<Split> splits, double totalAmount) {
        // Check if all splits are EqualSplit
        boolean allEqual = splits.stream().allMatch(s -> s instanceof EqualSplit);
        
        if (allEqual) {
            double equalAmount = totalAmount / splits.size();
            for (Split split : splits) {
                split.setAmount(equalAmount);
            }
        } else {
            // Check for PercentSplit and calculate amounts
            for (Split split : splits) {
                if (split instanceof PercentSplit) {
                    PercentSplit percentSplit = (PercentSplit) split;
                    double amount = (totalAmount * percentSplit.getPercent()) / 100.0;
                    split.setAmount(amount);
                }
                // ExactSplit already has amount set
            }
        }
    }

    public void showGroupExpenses(String groupId) {
        GroupService groupService = GroupService.getInstance();
        Group group = groupService.getGroup(groupId);
        
        if (group == null) {
            System.out.println("Group not found!");
            return;
        }
        
        System.out.println("\n=== Expenses for Group: " + group.getGroupName() + " ===");
        
        List<String> expenseIds = group.getExpenseIds();
        if (expenseIds.isEmpty()) {
            System.out.println("No expenses in this group.");
            return;
        }
        
        UserService userService = UserService.getInstance();
        for (Expense expense : expenses) {
            if (expenseIds.contains(expense.getExpenseId())) {
                User paidBy = userService.getUser(expense.getPaidBy());
                System.out.printf("- %s: Rs.%.2f (paid by %s)\n", 
                    expense.getDescription(), expense.getTotalAmount(), paidBy.getName());
            }
        }
    }

    public List<Expense> getAllExpenses() {
        return expenses;
    }

    public void showAllExpenses() {
        System.out.println("\n=== All Expenses ===");
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded.");
            return;
        }
        
        UserService userService = UserService.getInstance();
        for (Expense expense : expenses) {
            User paidBy = userService.getUser(expense.getPaidBy());
            System.out.printf("- %s: Rs.%.2f (paid by %s) [%s]\n", 
                expense.getDescription(), expense.getTotalAmount(), 
                paidBy.getName(), expense.getExpenseType());
        }
    }
}
