package models;

import java.util.*;

public interface SongRecommendation {

    Song recommendSong(String userId, List<Song> songLog);
}
