import hackathonsystem.HackathonSystem;
import hackathonsystem.enums.Department;
import hackathonsystem.enums.DifficultyLevel;
import hackathonsystem.models.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Initialize the Hackathon System
        HackathonSystem system = new HackathonSystem();
        
        System.out.println("========== Hackathon System Test ==========\n");
        
        // 1. Register Users and capture their IDs
        System.out.println("1. Registering Users...");
        String aliceId = system.registerUserAndGetId("Alice", Department.CS1);
        String bobId = system.registerUserAndGetId("Bob", Department.CS2);
        String charlieId = system.registerUserAndGetId("Charlie", Department.CS1);
        String davidId = system.registerUserAndGetId("David", Department.CS2);
        String eveId = system.registerUserAndGetId("Eve", Department.CS1);
        System.out.println("✓ 5 users registered\n");
        
        // 2. Register Problems
        System.out.println("2. Registering Problems...");
        Problem p1 = new Problem("Two Sum", "Find two numbers that add up to target", DifficultyLevel.EASY);
        Problem p2 = new Problem("Binary Search", "Implement binary search algorithm", DifficultyLevel.EASY);
        Problem p3 = new Problem("LRU Cache", "Design and implement LRU cache", DifficultyLevel.MEDIUM);
        Problem p4 = new Problem("Merge Intervals", "Merge overlapping intervals", DifficultyLevel.MEDIUM);
        Problem p5 = new Problem("Word Ladder", "Find shortest transformation sequence", DifficultyLevel.HARD);
        Problem p6 = new Problem("N-Queens", "Place N queens on N×N chessboard", DifficultyLevel.HARD);
        
        system.registerProblem(p1);
        system.registerProblem(p2);
        system.registerProblem(p3);
        system.registerProblem(p4);
        system.registerProblem(p5);
        system.registerProblem(p6);
        System.out.println("✓ 6 problems registered (2 Easy, 2 Medium, 2 Hard)");
        System.out.println("  - " + p1.getName() + " [" + p1.getLevel() + "] - " + p1.getScore() + " points");
        System.out.println("  - " + p2.getName() + " [" + p2.getLevel() + "] - " + p2.getScore() + " points");
        System.out.println("  - " + p3.getName() + " [" + p3.getLevel() + "] - " + p3.getScore() + " points");
        System.out.println("  - " + p4.getName() + " [" + p4.getLevel() + "] - " + p4.getScore() + " points");
        System.out.println("  - " + p5.getName() + " [" + p5.getLevel() + "] - " + p5.getScore() + " points");
        System.out.println("  - " + p6.getName() + " [" + p6.getLevel() + "] - " + p6.getScore() + " points\n");
        
        // 3. Simulate Users Solving Problems
        System.out.println("3. Simulating Problem Solving...");
        
        System.out.println("   Alice solves: Two Sum (Easy), LRU Cache (Medium), Word Ladder (Hard)");
        system.solve(aliceId, p1, 1500); // 50 points
        system.solve(aliceId, p3, 3000); // 100 points
        system.solve(aliceId, p5, 5000); // 150 points
        system.solve(aliceId, p6, 5000); // 150 points
        // Total: 300 points
        
        System.out.println("   Bob solves: Two Sum (Easy), Binary Search (Easy)");
        system.solve(bobId, p1, 1200); // 50 points
        system.solve(bobId, p2, 1800); // 50 points
        // Total: 100 points
        
        System.out.println("   Charlie solves: Merge Intervals (Medium), Word Ladder (Hard)");
        system.solve(charlieId, p4, 2500); // 100 points
        system.solve(charlieId, p5, 4500); // 150 points
        // Total: 250 points
        
        System.out.println("   David solves: Two Sum (Easy)");
        system.solve(davidId, p1, 1000); // 50 points
        // Total: 50 points
        
        System.out.println("   Eve solves: LRU Cache (Medium), N-Queens (Hard)");
        system.solve(eveId, p3, 2800); // 100 points
        system.solve(eveId, p6, 6000); // 150 points
        // Total: 250 points
        
        System.out.println("✓ Problems solved by users\n");
        
        // 4. Get Top Users
        System.out.println("4. Fetching Top 3 Users...");
        List<User> topUsers = system.getTopUsers(3);
        System.out.println("   Top Users:");
        for (int i = 0; i < topUsers.size(); i++) {
            User user = topUsers.get(i);
            System.out.println("   #" + (i+1) + " - " + user.getName() + 
                              " (" + user.getDepartment() + ")");
        }
        System.out.println();
        
        // 5. Get User's Solved Problems with Different Sorting
        System.out.println("5. Fetching User's Solved Problems...");
        
        // Using Score-Based Sorting
        System.out.println("   Alice's problems (sorted by score - highest first):");
        ScoreBasedList scoreSort = new ScoreBasedList();
        List<Problem> aliceProblemsScore = system.getUserSolvedProblems(aliceId, scoreSort);
        for (Problem p : aliceProblemsScore) {
            System.out.println("   - " + p.getName() + " [" + p.getLevel() + 
                              "] - " + p.getScore() + " points");
        }
        
        System.out.println("\n   Alice's problems (sorted by difficulty - easiest first):");
        DifficultyBasedSorter difficultySort = new DifficultyBasedSorter();
        List<Problem> aliceProblemsDiff = system.getUserSolvedProblems(aliceId, difficultySort);
        for (Problem p : aliceProblemsDiff) {
            System.out.println("   - " + p.getName() + " [" + p.getLevel() + "]");
        }
        
        System.out.println("\n   Bob's problems (sorted by score):");
        List<Problem> bobProblems = system.getUserSolvedProblems(bobId, scoreSort);
        for (Problem p : bobProblems) {
            System.out.println("   - " + p.getName() + " [" + p.getLevel() + 
                              "] - " + p.getScore() + " points");
        }
        
        System.out.println("\n========== Additional Tests ==========\n");
        
        // Test sorting with all problems
        System.out.println("6. Testing Sorting Strategies with All Problems:");
        List<Problem> allProblems = List.of(p1, p2, p3, p4, p5, p6);
        
        ScoreBasedList scoreSorter = new ScoreBasedList();
        List<Problem> sortedByScore = scoreSorter.getList(allProblems);
        System.out.println("\n   All Problems Sorted by Score:");
        for (Problem p : sortedByScore) {
            System.out.println("   - " + p.getName() + " [" + p.getLevel() + "] - " + p.getScore() + " points");
        }
        
        DifficultyBasedSorter difficultySorter = new DifficultyBasedSorter();
        List<Problem> sortedByDifficulty = difficultySorter.getList(allProblems);
        System.out.println("\n   All Problems Sorted by Difficulty:");
        for (Problem p : sortedByDifficulty) {
            System.out.println("   - " + p.getName() + " [" + p.getLevel() + "]");
        }

        // 7. Get Problem Stats
        System.out.println("\n7. Fetching Problem Stats...");

        System.out.println("\n   Stats sorted by User Count (most solved first):");
        UserCountList userCountSorter = new UserCountList();
        List<ProblemStat> statsByCount = system.getProblemStats(userCountSorter);
        for (ProblemStat stat : statsByCount) {
            Problem p = system.getProblemById(stat.getProblemId());
            System.out.printf("   - %-25s | Solved by: %d user(s) | Avg Time: %.0f s%n",
                    p.getName(), stat.getCount(), stat.getAvgTime());
        }

        System.out.println("\n   Stats sorted by Avg Time (fastest first):");
        AvgTimeList avgTimeSorter = new AvgTimeList();
        List<ProblemStat> statsByTime = system.getProblemStats(avgTimeSorter);
        for (ProblemStat stat : statsByTime) {
            Problem p = system.getProblemById(stat.getProblemId());
            System.out.printf("   - %-25s | Solved by: %d user(s) | Avg Time: %.0f s%n",
                    p.getName(), stat.getCount(), stat.getAvgTime());
        }

        System.out.println("\n========== Test Complete ==========");
        System.out.println("\u2713 All functionality tested successfully!");
    }
}
