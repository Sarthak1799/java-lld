package hackathonsystem.models;
import java.util.*
;
public class DifficultyBasedSorter implements ProblemListInterface {
    @Override
    public List<Problem> getList(List<Problem> problems){
        List<Problem> newList = new ArrayList<>(problems);
        Collections.sort(newList, (a,b) -> a.getLevel().compareTo(b.getLevel()));
        return newList;
    }
}