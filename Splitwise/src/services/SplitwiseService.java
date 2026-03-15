package services;

import enums.ExpenseType;
import models.Balance;
import models.Expense;
import models.Group;
import models.User;
import splitstrategies.Split;

import java.util.List;
import java.util.Map;

/**
 * Facade/Orchestrator layer that provides a unified interface to all Splitwise
 * services. Implements the Facade Design Pattern.
 */
public class SplitwiseService {

    private UserService userService;
    private GroupService groupService;
    private ExpenseService expenseService;
    private BalanceService balanceService;
    private static SplitwiseService instance;

    private SplitwiseService() {
        this.userService = UserService.getInstance();
        this.groupService = GroupService.getInstance();
        this.expenseService = ExpenseService.getInstance();
        this.balanceService = BalanceService.getInstance();
    }

    public static SplitwiseService getInstance() {
        if (instance == null) {
            instance = new SplitwiseService();
        }
        return instance;
    }

    // ==================== User Operations ====================
    public void addUser(User user) {
        userService.addUser(user);
    }

    public User getUser(String userId) {
        return userService.getUser(userId);
    }

    public Map<String, User> getAllUsers() {
        return userService.getAllUsers();
    }

    public boolean userExists(String userId) {
        return userService.userExists(userId);
    }

    // ==================== Group Operations ====================
    public void createGroup(Group group) {
        groupService.createGroup(group);
    }

    public Group getGroup(String groupId) {
        return groupService.getGroup(groupId);
    }

    public void addMemberToGroup(String groupId, String userId) {
        groupService.addMemberToGroup(groupId, userId);
    }

    public Map<String, Group> getAllGroups() {
        return groupService.getAllGroups();
    }

    // ==================== Expense Operations ====================
    public void addExpense(String description, double totalAmount, String paidBy,
            List<Split> splits, ExpenseType expenseType, String groupId) {
        expenseService.addExpense(description, totalAmount, paidBy, splits, expenseType, groupId);
    }

    public void showGroupExpenses(String groupId) {
        expenseService.showGroupExpenses(groupId);
    }

    public List<Expense> getAllExpenses() {
        return expenseService.getAllExpenses();
    }

    public void showAllExpenses() {
        expenseService.showAllExpenses();
    }

    // ==================== Balance Operations ====================
    public void settleBalance(String userId1, String userId2, double amount) {
        balanceService.settleBalance(userId1, userId2, amount);
    }

    public List<Balance> getUserBalances(String userId) {
        return balanceService.getUserBalances(userId);
    }

    public void showUserBalances(String userId) {
        balanceService.showUserBalances(userId);
    }

    public void showAllBalances() {
        balanceService.showAllBalances();
    }

    // ==================== Composite Operations ====================
    /**
     * Create a user and add them to a group in one operation
     */
    public void addUserToGroup(User user, String groupId) {
        if (!userService.userExists(user.getUserId())) {
            userService.addUser(user);
        }
        groupService.addMemberToGroup(groupId, user.getUserId());
    }

    /**
     * Create a group with initial members
     */
    public void createGroupWithMembers(Group group, List<String> memberIds) {
        for (String memberId : memberIds) {
            group.addMember(memberId);
        }
        groupService.createGroup(group);
    }

    /**
     * Get complete balance summary for a user across all groups
     */
    public void showCompleteUserSummary(String userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            System.out.println("User not found!");
            return;
        }

        System.out.println("\n=== Complete Summary for " + user.getName() + " ===");

        // Show groups
        System.out.println("\nGroups:");
        List<String> groupIds = user.getGroupIds();
        if (groupIds.isEmpty()) {
            System.out.println("  Not a member of any groups");
        } else {
            for (String groupId : groupIds) {
                Group group = groupService.getGroup(groupId);
                System.out.println("  - " + group.getGroupName() + " (" + group.getMemberIds().size() + " members)");
            }
        }

        // Show balances
        balanceService.showUserBalances(userId);
    }
}
