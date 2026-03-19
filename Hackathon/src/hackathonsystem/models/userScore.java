package hackathonsystem.models;

import java.util.Objects;

public class userScore implements Comparable<userScore> {
    private int score;
    private String userId;

    public userScore(int score, String id){
        this.userId = id;
        this.score = score;
    }

    public String getId(){
        return userId;
    }

    public int getScore(){
        return score;
    }

    public void setScore(int score){
        this.score = score;
    }

    @Override
    public int compareTo(userScore other) {
        // Sort by score descending, then by userId ascending for tie-breaking
        if (this.score != other.score) {
            return Integer.compare(other.score, this.score);
        }
        return this.userId.compareTo(other.userId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        userScore that = (userScore) obj;
        return userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
