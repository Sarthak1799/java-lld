package hackathonsystem.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreBasedList implements ProblemListInterface {
    @Override
    public List<Problem> getList(List<Problem> problems){
        List<Problem> newList = new ArrayList<>(problems);
        Collections.sort(newList, (a,b) -> b.getScore() - a.getScore());
        return newList;
    }
}
