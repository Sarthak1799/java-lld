package hackathonsystem.services;

import hackathonsystem.models.*;
import java.util.*;

public class ProblemSetService {
    private Map<String, Problem> problemSet;
    private Map<String, ProblemStat> stats;
    private static ProblemSetService instance = null;

    public ProblemSetService(){
        this.problemSet = new HashMap<>();
        this.stats = new HashMap<>();
    }

    public static ProblemSetService getInstance(){
        if(instance == null)
            instance = new ProblemSetService();
        return instance;
    }

    public void addProblem(Problem problem){
        this.problemSet.put(problem.getId(), problem);
        this.stats.put(problem.getId(), new ProblemStat(problem.getId()));
    }

    public void increaseProblemCount(String problemId, long time){
        this.stats.putIfAbsent(problemId, new ProblemStat(problemId));
        this.stats.get(problemId).incrementCount(time);
    }

    public ProblemStat getStat(String problemId){
        return this.stats.get(problemId);
    }

    public Problem getProblem(String problemId){
        return this.problemSet.get(problemId);
    }

    public List<ProblemStat> listStats(){
        List<ProblemStat> list = new ArrayList<>(stats.values());
        return list;
    }

}
