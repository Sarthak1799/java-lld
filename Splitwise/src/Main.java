
import enums.ExpenseType;
import java.util.Arrays;
import java.util.List;
import models.Group;
import models.User;
import services.SplitwiseService;
import splitstrategies.EqualSplit;
import splitstrategies.ExactSplit;
import splitstrategies.PercentSplit;
import splitstrategies.Split;

public class Main {

    public static void main(String[] args) {
        // Initialize the orchestrator service (Facade Pattern)
        SplitwiseService splitwise = SplitwiseService.getInstance();

        System.out.println("=== Splitwise Application Demo ===\n");

        // 1. Create Users
        System.out.println("1. Creating users...");
        User alice = new User("U1", "Alice", "alice@example.com", "1234567890");
        User bob = new User("U2", "Bob", "bob@example.com", "9876543210");
        User charlie = new User("U3", "Charlie", "charlie@example.com", "5555555555");
        User david = new User("U4", "David", "david@example.com", "6666666666");

        splitwise.addUser(alice);
        splitwise.addUser(bob);
        splitwise.addUser(charlie);
        splitwise.addUser(david);
        System.out.println("Created users: Alice, Bob, Charlie, David\n");

        // 2. Create a Group
        System.out.println("2. Creating a group...");
        Group roommates = new Group("G1", "Roommates");
        roommates.addMember("U1"); // Alice
        roommates.addMember("U2"); // Bob
        roommates.addMember("U3"); // Charlie
        splitwise.createGroup(roommates);
        System.out.println("Created group 'Roommates' with Alice, Bob, and Charlie\n");

        // 3. Add an EQUAL split expense in the group
        System.out.println("3. Adding an equal split expense (Rent)...");
        List<Split> rentSplits = Arrays.asList(
                new EqualSplit("U1"), // Alice
                new EqualSplit("U2"), // Bob
                new EqualSplit("U3") // Charlie
        );
        splitwise.addExpense("Rent", 3000.0, "U1", rentSplits, ExpenseType.GROUP, "G1");

        // 4. Show balances after rent
        splitwise.showAllBalances();

        // 5. Add an EXACT split expense (Groceries)
        System.out.println("\n4. Adding an exact split expense (Groceries)...");
        List<Split> grocerySplits = Arrays.asList(
                new ExactSplit("U1", 200.0), // Alice
                new ExactSplit("U2", 300.0), // Bob
                new ExactSplit("U3", 100.0) // Charlie
        );
        splitwise.addExpense("Groceries", 600.0, "U2", grocerySplits, ExpenseType.GROUP, "G1");

        // 6. Show balances after groceries
        splitwise.showAllBalances();

        // 7. Add a PERCENT split expense (Restaurant)
        System.out.println("\n5. Adding a percent split expense (Restaurant)...");
        List<Split> restaurantSplits = Arrays.asList(
                new PercentSplit("U1", 50.0), // Alice pays 50%
                new PercentSplit("U2", 30.0), // Bob pays 30%
                new PercentSplit("U3", 20.0) // Charlie pays 20%
        );
        splitwise.addExpense("Restaurant Dinner", 1000.0, "U3", restaurantSplits, ExpenseType.GROUP, "G1");

        // 8. Show balances after restaurant
        splitwise.showAllBalances();

        // 9. Add an INDIVIDUAL expense (outside group)
        System.out.println("\n6. Adding an individual expense (Movie)...");
        List<Split> movieSplits = Arrays.asList(
                new EqualSplit("U1"), // Alice
                new EqualSplit("U4") // David
        );
        splitwise.addExpense("Movie Tickets", 400.0, "U4", movieSplits, ExpenseType.INDIVIDUAL, null);

        // 10. Show individual user balances
        System.out.println("\n7. Showing individual user balances:");
        splitwise.showUserBalances("U1"); // Alice's balances
        splitwise.showUserBalances("U2"); // Bob's balances
        splitwise.showUserBalances("U4"); // David's balances

        // 11. Show group expenses
        splitwise.showGroupExpenses("G1");

        // 12. Settle up - Bob pays Alice
        System.out.println("\n8. Bob settles his debt with Alice...");
        splitwise.settleBalance("U2", "U1", 700.0);

        // 13. Show balances after settlement
        splitwise.showAllBalances();

        // 14. Show all expenses
        splitwise.showAllExpenses();

        // 15. Create another group and add expense
        System.out.println("\n9. Creating another group for a trip...");
        Group tripGroup = new Group("G2", "Goa Trip");
        tripGroup.addMember("U1"); // Alice
        tripGroup.addMember("U2"); // Bob
        tripGroup.addMember("U3"); // Charlie
        tripGroup.addMember("U4"); // David
        splitwise.createGroup(tripGroup);

        List<Split> hotelSplits = Arrays.asList(
                new EqualSplit("U1"),
                new EqualSplit("U2"),
                new EqualSplit("U3"),
                new EqualSplit("U4")
        );
        splitwise.addExpense("Hotel Booking", 8000.0, "U1", hotelSplits, ExpenseType.GROUP, "G2");

        // 16. Final balances
        splitwise.showAllBalances();

        System.out.println("\n=== Demo Complete ===");
    }
}
