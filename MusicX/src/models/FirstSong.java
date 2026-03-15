package models;

import java.util.*;

public class FirstSong implements SongRecommendation {

    @Override
    public Song recommendSong(String userId, List<Song> songLog) {
        if (songLog == null || songLog.isEmpty()) {
            throw new IllegalArgumentException("Song log is empty");
        }
        return songLog.get(0);
    }
}
