package hackathonsystem.models;
import java.util.*;

public class AvgTimeList implements ProblemStatListInterface {
    @Override
    public List<ProblemStat> getList(List<ProblemStat> problems){
        List<ProblemStat> newList = new ArrayList<>(problems);
        Collections.sort(newList, (a, b) -> Double.compare(a.getAvgTime(), b.getAvgTime()));
        return newList;
    }
}
