package models;

import java.awt.print.Printable;
import java.util.*;

public class MusicPlayer {

    private String userId;
    private Song currentSong;
    private boolean isPlaying;
    private int currentPosition; // in seconds

    public MusicPlayer(String userId) {
        this.userId = userId;
        this.currentSong = null;
        this.currentPosition = 0;
        this.isPlaying = false;
    }

    public void setSong(Song song) {
        this.currentSong = song;
        this.currentPosition = 0;
    }

    public Song getCurrentSong() {
        return currentSong;
    }

    public void seek(int position) {
        if (currentSong != null && position >= 0 && position <= currentSong.getDuration()) {
            this.currentPosition = position;
        }
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void play() {
        if (currentSong != null) {
            isPlaying = true;
        }
    }

    public void pause() {
        isPlaying = false;
    }
}
