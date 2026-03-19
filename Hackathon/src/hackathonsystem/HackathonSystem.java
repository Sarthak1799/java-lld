package hackathonsystem;

import hackathonsystem.enums.Department;
import hackathonsystem.models.Problem;
import hackathonsystem.models.ProblemListInterface;
import hackathonsystem.models.ProblemStat;
import hackathonsystem.models.ProblemStatListInterface;
import hackathonsystem.models.User;
import hackathonsystem.models.userScore;
import hackathonsystem.services.ProblemSetService;
import hackathonsystem.services.UserService;
import java.util.*;

public class HackathonSystem {
    private UserService userService;
    private ProblemSetService problemSetService;
    private TreeSet<userScore> scoreList; // Maintains sorted order automatically
    private HashMap<String, userScore> userScoreMap; // For O(1) lookup by userId

    public HackathonSystem(){
        this.userService = UserService.getInstance();
        this.problemSetService = ProblemSetService.getInstance();
        this.scoreList = new TreeSet<>();
        this.userScoreMap = new HashMap<>();
    }

    public void registerProblem(Problem problem){
        this.problemSetService.addProblem(problem);
    }

    public void registerUser(String name, Department department){
        this.userService.registerUser(name, department);
    }
    
    public String registerUserAndGetId(String name, Department department){
        return this.userService.registerUserAndGetId(name, department);
    }

    public void solve(String userId, Problem problem, long time){
        this.userService.registerProblem(userId, problem);
        this.problemSetService.increaseProblemCount(problem.getId(), time);
        int problemScore = problem.getScoreForDifficulty(problem.getLevel());
        
        // Get existing userScore in O(1) time using HashMap
        userScore existingScore = userScoreMap.get(userId);
        
        if (existingScore != null) {
            // Remove from TreeSet, update score, add back to TreeSet
            scoreList.remove(existingScore);
            existingScore.setScore(existingScore.getScore() + problemScore);
            scoreList.add(existingScore);
        } else {
            // Create new entry and add to both HashMap and TreeSet
            userScore newScore = new userScore(problemScore, userId);
            userScoreMap.put(userId, newScore);
            scoreList.add(newScore);
        }
    }

    public List<User> getTopUsers(int n){
        // TreeSet already maintains sorted order, just get top n
        List<User> users = new ArrayList<>();
        int count = 0;
        
        for(userScore us : scoreList){
            User user = this.userService.getUser(us.getId());
            users.add(user);
            count++;
            if(count == n)
                break;
        }

        return users;
    }

    public List<Problem> getUserSolvedProblems(String userId, ProblemListInterface sorter){
        List<Problem> problems = this.userService.getProblems(userId);
        problems = sorter.getList(problems);
        return problems;

    }

    public List<ProblemStat> getProblemStats(ProblemStatListInterface sorter){
        List<ProblemStat> stats = this.problemSetService.listStats();
        stats = sorter.getList(stats);
        return stats;
    }

    public Problem getProblemById(String problemId){
        return this.problemSetService.getProblem(problemId);
    }

}
