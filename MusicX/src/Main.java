
import services.*;
import models.*;

public class Main {

    public static void main(String[] args) {
        // Initialize the MusicX System
        MusicXSystem system = MusicXSystem.getInstance();

        System.out.println("=== MusicX System Demo ===\n");

        // Register a new user
        System.out.println("1. Registering a new user...");
        String userId = system.registerUser("john_doe", "john@example.com", "password123");
        System.out.println("User registered with ID: " + userId + "\n");

        // Login
        System.out.println("2. Logging in...");
        system.login(userId, "password123");
        System.out.println("User logged in successfully!\n");

        // Get music player
        MusicPlayer player = system.getMusicPlayer(userId);
        System.out.println("3. Music player retrieved for user\n");

        // Find and play a song
        System.out.println("4. Searching for song 'Come Together'...");
        Song song1 = system.findSongByName("Come Together");
        if (song1 != null) {
            System.out.println("Song found: " + song1.getTitle());

            // Set song in player
            player.setSong(song1);
            player.play();
            System.out.println("Now playing: " + song1.getTitle());

            // REGISTER SONG WITH USER
            system.registerSongForUser(userId, song1);
            System.out.println("Song registered to user's listening history\n");
        }

        // Play another song
        System.out.println("5. Searching for song 'Hello'...");
        Song song2 = system.findSongByName("Hello");
        if (song2 != null) {
            System.out.println("Song found: " + song2.getTitle());

            // Set song in player
            player.setSong(song2);
            player.play();
            System.out.println("Now playing: " + song2.getTitle());

            // REGISTER SONG WITH USER
            system.registerSongForUser(userId, song2);
            System.out.println("Song registered to user's listening history\n");
        }

        // Play the first song again
        System.out.println("6. Playing 'Come Together' again...");
        if (song1 != null) {
            player.setSong(song1);
            player.play();
            System.out.println("Now playing: " + song1.getTitle());

            // REGISTER SONG WITH USER AGAIN
            system.registerSongForUser(userId, song1);
            System.out.println("Song registered to user's listening history\n");
        }

        // Demonstrate pause and seek
        System.out.println("7. Testing player controls...");
        player.seek(30);
        System.out.println("Seeked to position: " + player.getCurrentPosition() + " seconds");
        player.pause();
        System.out.println("Player paused: " + !player.isPlaying() + "\n");

        // Test song recommendation - First Song
        System.out.println("8. Getting song recommendation (First Song Strategy)...");
        try {
            Song recommendedSong = system.recommendSong(userId, new FirstSong());
            System.out.println("Recommended song (First): " + recommendedSong.getTitle());

            // Play recommended song
            player.setSong(recommendedSong);
            player.play();
            System.out.println("Now playing recommended song: " + recommendedSong.getTitle());

            // REGISTER RECOMMENDED SONG
            system.registerSongForUser(userId, recommendedSong);
            System.out.println("Song registered to user's listening history\n");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }

        // Test song recommendation - Most Played Song
        System.out.println("9. Getting song recommendation (Most Played Song Strategy)...");
        try {
            Song mostPlayedSong = system.recommendSong(userId, new MostPlayedSong());
            System.out.println("Recommended song (Most Played): " + mostPlayedSong.getTitle());

            // Play most played song
            player.setSong(mostPlayedSong);
            player.play();
            System.out.println("Now playing recommended song: " + mostPlayedSong.getTitle());

            // REGISTER RECOMMENDED SONG
            system.registerSongForUser(userId, mostPlayedSong);
            System.out.println("Song registered to user's listening history\n");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }

        // Display user's listening history
        System.out.println("10. User's listening history:");
        displayListeningHistory(system, userId);

        // Logout
        System.out.println("\n11. Logging out...");
        system.logout(userId);
        System.out.println("User logged out successfully!");

        System.out.println("\n=== Demo Complete ===");
    }

    private static void displayListeningHistory(MusicXSystem system, String userId) {
        var songLog = system.getUserService().getUserSongLog(userId);
        System.out.println("Total songs played: " + songLog.size());
        int count = 1;
        for (Song song : songLog) {
            System.out.println("  " + count + ". " + song.getTitle());
            count++;
        }
    }
}
