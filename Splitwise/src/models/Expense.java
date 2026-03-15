package models;

import enums.ExpenseType;
import java.util.Date;
import java.util.List;
import splitstrategies.Split;

public class Expense {

    private String expenseId;
    private String description;
    private double totalAmount;
    private String paidBy;  // userId who paid
    private List<Split> splits;
    private ExpenseType expenseType;
    private String groupId;  // null if individual expense
    private Date createdAt;

    public Expense(String expenseId, String description, double totalAmount,
            String paidBy, List<Split> splits, ExpenseType expenseType, String groupId) {
        this.expenseId = expenseId;
        this.description = description;
        this.totalAmount = totalAmount;
        this.paidBy = paidBy;
        this.splits = splits;
        this.expenseType = expenseType;
        this.groupId = groupId;
        this.createdAt = new Date();
    }

    public String getExpenseId() {
        return expenseId;
    }

    public String getDescription() {
        return description;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public List<Split> getSplits() {
        return splits;
    }

    public ExpenseType getExpenseType() {
        return expenseType;
    }

    public String getGroupId() {
        return groupId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public boolean validate() {
        // Validate that the sum of splits equals total amount
        double splitSum = 0;
        for (Split split : splits) {
            splitSum += split.getAmount();
        }
        return Math.abs(splitSum - totalAmount) < 0.01; // Allow small floating point errors
    }

    @Override
    public String toString() {
        return "Expense{"
                + "expenseId='" + expenseId + '\''
                + ", description='" + description + '\''
                + ", totalAmount=" + totalAmount
                + ", paidBy='" + paidBy + '\''
                + ", expenseType=" + expenseType
                + '}';
    }
}
