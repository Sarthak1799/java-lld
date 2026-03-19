package hackathonsystem.models;
import hackathonsystem.enums.*;
import java.util.UUID;

public class Problem {
    private String id;
    private String name;
    private String data;
    private DifficultyLevel level;
    private int score;

    public int getScoreForDifficulty(DifficultyLevel level){
        if(level.equals( DifficultyLevel.EASY))
            return 50;
        else if(level.equals(DifficultyLevel.MEDIUM))
            return 100;
        else
            return 150;

    }

    public Problem(String name, String data, DifficultyLevel level){
        this.name = name;
        this.data = data;
        this.level = level;
        this.score = getScoreForDifficulty(level);
        this.id = "Prb-" + UUID.randomUUID().toString();
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public DifficultyLevel getLevel(){
        return level;
    }

    public int getScore(){
        return score;
    }
}
