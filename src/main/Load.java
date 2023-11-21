package main;

import java.util.ArrayList;
import java.util.LinkedList;

public class Load extends Command {
    private String message;
    public void returnLoad (Command command, Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        ArrayList<Users> users = library.getUsers();
        for (Users user : users) {
            if (user.getUsername().equals(command.getUsername())) {
                if (!user.isSomethingSelected()) {
                    message = "Please select a source before attempting to load.";
                } else {
                    message = "Playback loaded successfully.";
                    user.setSomethingLoaded(true);
                    user.getMusicPlayer().setPlayTimestamp(command.getTimestamp());
                    user.getMusicPlayer().setPaused(false);
                    // Setup for when a song is played by a user
                    if (user.getTrackType() == Users.Track.SONG) {
                        Songs playerSong = user.getMusicPlayer().getSong();

                        user.getMusicPlayer().setRemainedTime(playerSong.getDuration());
                    } else if (user.getTrackType() == Users.Track.PLAYLIST) {
                        if (user.getMusicPlayer().getPlaylist().getSongs().isEmpty())
                            continue;
                        // Get the first song from that playlist
                        Songs playerSong = user.getMusicPlayer().getPlaylist().getSongs().get(0);

                        user.getMusicPlayer().setRemainedTime(playerSong.getDuration());
                        // Set the first song on the player
                        user.getMusicPlayer().setSong(playerSong);
                    } else {
                        // Get the first episode from the podcast
                        Episodes playerEpisode = user.getMusicPlayer().getPodcast().getEpisodes().get(0);

                        user.getMusicPlayer().setRemainedTime(playerEpisode.getDuration());
                        // Set the first episode on the player
                        user.getMusicPlayer().setEpisode(playerEpisode);
                    }
                    user.setSomethingSelected(false);
                }
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
