package models;

import java.util.*;

public class MostPlayedSong implements SongRecommendation {

    @Override
    public Song recommendSong(String userId, List<Song> songLog) {
        if (songLog == null || songLog.isEmpty()) {
            throw new IllegalArgumentException("Song log is empty");
        }
        Map<String, Integer> playCount = new HashMap<>();
        int maxCount = 0;
        Song mostPlayedSong = null;

        for (Song song : songLog) {
            playCount.putIfAbsent(song.getId(), 0);
            playCount.put(song.getId(), playCount.get(song.getId()) + 1);
            if (playCount.get(song.getId()) > maxCount) {
                maxCount = playCount.get(song.getId());
                mostPlayedSong = song;
            }
        }

        return mostPlayedSong;
    }
}
