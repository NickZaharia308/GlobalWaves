package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GetTop5Songs extends Command {
    private String message;
    private ArrayList<Songs> songs = new ArrayList<>();

    public void returnGetTop5Songs (Command command, Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        ArrayList<Songs> songsTopFive = new ArrayList<>(library.getSongs());

        // Sort songs based on the number of likes
        songsTopFive.sort(Comparator.comparingLong(Songs::getNumberOfLikes).reversed());

        // Get the top 5 songs
        int topCount = Math.min(5, songsTopFive.size());

        for (int i = 0; i < topCount; i++) {
            songs.add(songsTopFive.get(i));
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Songs> getSongs() {
        return songs;
    }

}
