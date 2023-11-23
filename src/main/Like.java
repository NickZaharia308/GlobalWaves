package main;

import java.util.ArrayList;

public class Like extends Command {
    private String message;
    public void returnLike (Command command, Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        // Finding the user
        ArrayList<Users> users = library.getUsers();
        Users user = null;
        for (Users user1 : users) {
            if (user1.getUsername().equals(command.getUsername())) {
                user = user1;
                break;
            }
        }

        // If there is nothing loaded
        if (!user.isSomethingLoaded()) {
            message = "Please load a source before liking or unliking.";
            return;
        }

        // If the loaded source is not a song
        if (user.getMusicPlayer().getSong() == null) {
            message = "Loaded source is not a song.";
            return;
        }
        Status status = new Status();
        status.returnStatus(command, library);

        // Finding the song in Songs collection
        ArrayList<Songs> songs = library.getSongs();
        Songs UserSong = user.getMusicPlayer().getSong();
        ArrayList<Songs> likedSongs = user.getLikedSongs();
        for (Songs song: songs) {
            if (song.getName().equals(UserSong.getName())) {
                for (Songs song2 : likedSongs) {
                    if (song2.getName().equals(UserSong.getName())) {
                        // Remove the like from the HashMap
                        song.getUserLikesMap().put(user.getUsername(), false);
                        // Remove the song from the liked playlist
                        likedSongs.remove(song2);
                        setMessage("Unlike registered successfully.");
                        return;
                    }
                }
                // Add the player's like in the HashMap
                song.getUserLikesMap().put(user.getUsername(), true);

                // Add the song to liked playlist
                likedSongs.add(song);

                setMessage("Like registered successfully.");
                break;
            }
        }



    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
