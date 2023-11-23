package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GetTop5Playlists extends Command {
    private String message;
    private ArrayList<Playlists> playlists = new ArrayList<>();

    public void returnGetTop5Playlists (Command command, Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        ArrayList<Playlists> playlistsTopFive = new ArrayList<>(library.getPlaylists());

        // Sort playlists based on followers count
        playlistsTopFive.sort(Comparator.comparingInt(Playlists::getFollowers).reversed());

        // Get the top 5 playlists or less
        int topCount = Math.min(5, playlistsTopFive.size());

        for (int i = 0; i < topCount; i++) {
            playlists.add(playlistsTopFive.get(i));
        }

    }

        public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Playlists> getPlaylists() {
        return playlists;
    }
}
