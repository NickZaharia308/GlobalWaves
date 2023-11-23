package main;

import java.util.ArrayList;

public class Prev extends Command {
    private String message;

    public void returnPrev (Command command, Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        ArrayList<Users> users = library.getUsers();
        Users user = users.get(1);
        for (Users user1 : users) {
            if (user1.getUsername().equals(command.getUsername())) {
                user = user1;
                break;
            }
        }

        Status status = new Status();
        status.returnStatus(command, library);

        // If there is nothing loaded
        if (!user.isSomethingLoaded()) {
            setMessage("Please load a source before returning to the previous track.");
            return;
        }
        user.getMusicPlayer().setPaused(false);

        Users.Track track = user.getTrackType();
        switch (track) {
            case SONG:
                // Replay the song from the start
                user.getMusicPlayer().setRemainedTime(user.getMusicPlayer().getSong().getDuration());
                user.getMusicPlayer().setPlayTimestamp(command.getTimestamp());
                setMessage("Returned to previous track successfully. The current track is " + user.getMusicPlayer().getSong().getName() + ".");
                break;

            case PLAYLIST:
                Playlists currentPlaylist = user.getMusicPlayer().getPlaylist();
                Songs currentSong = user.getMusicPlayer().getSong();

                if (currentPlaylist.getSongs().isEmpty())
                    return;
                // If the song is the first in the playlist
                if (currentSong.getName().equals(currentPlaylist.getSongs().get(0).getName())) {
                    user.getMusicPlayer().setRemainedTime(currentSong.getDuration());
                    user.getMusicPlayer().setPlayTimestamp(command.getTimestamp());
                    setMessage("Returned to previous track successfully. The current track is " + currentSong.getName() + ".");
                    break;
                }

                // If not a single second has passed (Song at start)
                if (currentSong.getDuration() == user.getMusicPlayer().getRemainedTime()) {
                    // Get the previous song
                    int position = currentPlaylist.getSongs().indexOf(currentSong);
                    currentSong = currentPlaylist.getSongs().get(position - 1);

                    // Set the new song on the MusicPlayer
                    user.getMusicPlayer().setSong(currentSong);
                    user.getMusicPlayer().setRemainedTime(currentSong.getDuration());
                    user.getMusicPlayer().setPlayTimestamp(command.getTimestamp());
                    setMessage("Returned to previous track successfully. The current track is " + currentSong.getName() + ".");
                } else {
                    user.getMusicPlayer().setRemainedTime(currentSong.getDuration());
                    user.getMusicPlayer().setPlayTimestamp(command.getTimestamp());
                    setMessage("Returned to previous track successfully. The current track is " + currentSong.getName() + ".");
                }
                break;

            case PODCAST:
                Podcasts currentPodcast = user.getMusicPlayer().getPodcast();
                Episodes currentEpisode = user.getMusicPlayer().getEpisode();

                // If the episode is the first in the podcast
                if (currentEpisode.getName().equals(currentPodcast.getEpisodes().get(0).getName())) {
                    user.getMusicPlayer().setRemainedTime(currentEpisode.getDuration());
                    currentEpisode.setRemainingTime(currentEpisode.getDuration());

                    user.getMusicPlayer().setPlayTimestamp(command.getTimestamp());
                    setMessage("Returned to previous track successfully. The current track is " + currentEpisode.getName() + ".");
                    break;
                }

                // If not a single second has passed (episode at start)
                if (currentEpisode.getDuration() == currentEpisode.getRemainingTime()) {
                    // Get the previous song
                    int position = currentPodcast.getEpisodes().indexOf(currentEpisode);
                    currentEpisode = currentPodcast.getEpisodes().get(position - 1);

                    // Set the new episode on the MusicPlayer
                    currentEpisode.setRemainingTime(currentEpisode.getDuration());
                    user.getMusicPlayer().setEpisode(currentEpisode);
                    user.getMusicPlayer().setRemainedTime(currentEpisode.getDuration());
                    user.getMusicPlayer().setPlayTimestamp(command.getTimestamp());
                    setMessage("Returned to previous track successfully. The current track is " + currentEpisode.getName() + ".");
                } else {
                    user.getMusicPlayer().setRemainedTime(currentEpisode.getDuration());
                    currentEpisode.setRemainingTime(currentEpisode.getDuration());

                    user.getMusicPlayer().setPlayTimestamp(command.getTimestamp());
                    setMessage("Returned to previous track successfully. The current track is " + currentEpisode.getName() + ".");
                }
                break;

            default:
                break;
        }

    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
