package hackathonsystem.models;

public class ProblemStat {
    private String problemId;
    private int userCount;
    private double avgTime;
    private long totalTime;

    public ProblemStat(String problemId){
        this.problemId = problemId;
        this.userCount = 0;
        this.avgTime = 0;
    }

    public void incrementCount(long time){
        this.userCount++;
        this.totalTime += time; 
        this.avgTime = totalTime/userCount;
    }

    public String getProblemId(){
        return problemId;
    }

    public double getAvgTime(){
        return avgTime;
    }

    public int getCount(){
        return userCount;
    }
}
