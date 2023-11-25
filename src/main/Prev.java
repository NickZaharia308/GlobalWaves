package main;

import lombok.Getter;

@Getter
public class Prev extends Command {
    private String message;

    /**
     * Returns to the previous track in the user's music player.
     *
     * @param command  The command containing information about the previous track operation.
     * @param library  The library containing playlists, songs, and user information.
     */
    public void returnPrev(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());

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
                user.getMusicPlayer().setRemainedTime(user.getMusicPlayer().getSong().
                     getDuration());
                user.getMusicPlayer().setPlayTimestamp(command.getTimestamp());
                setMessage("Returned to previous track successfully. The current track is "
                            + user.getMusicPlayer().getSong().getName() + ".");
                break;

            case PLAYLIST:
                Playlists currentPlaylist = user.getMusicPlayer().getPlaylist();
                Songs currentSong = user.getMusicPlayer().getSong();

                if (currentPlaylist.getSongs().isEmpty()) {
                    return;
                }
                // If the song is the first in the playlist
                if (currentSong.getName().equals(currentPlaylist.getSongs().get(0).getName())) {
                    user.getMusicPlayer().setRemainedTime(currentSong.getDuration());
                    user.getMusicPlayer().setPlayTimestamp(command.getTimestamp());
                    setMessage("Returned to previous track successfully. The current track is "
                                + currentSong.getName() + ".");
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
                    setMessage("Returned to previous track successfully. The current track is "
                                + currentSong.getName() + ".");
                } else {
                    user.getMusicPlayer().setRemainedTime(currentSong.getDuration());
                    user.getMusicPlayer().setPlayTimestamp(command.getTimestamp());
                    setMessage("Returned to previous track successfully. The current track is "
                                + currentSong.getName() + ".");
                }
                break;

            case PODCAST:
                Podcasts currentPodcast = user.getMusicPlayer().getPodcast();
                Episodes currentEpisode = user.getMusicPlayer().getEpisode();

                // If the episode is the first in the podcast
                if (currentEpisode.getName().equals(currentPodcast.getEpisodes().get(0).
                                                    getName())) {
                    user.getMusicPlayer().setRemainedTime(currentEpisode.getDuration());
                    currentEpisode.setRemainingTime(currentEpisode.getDuration());

                    user.getMusicPlayer().setPlayTimestamp(command.getTimestamp());
                    setMessage("Returned to previous track successfully. The current track is "
                                + currentEpisode.getName() + ".");
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
                    setMessage("Returned to previous track successfully. The current track is "
                                + currentEpisode.getName() + ".");
                } else {
                    user.getMusicPlayer().setRemainedTime(currentEpisode.getDuration());
                    currentEpisode.setRemainingTime(currentEpisode.getDuration());

                    user.getMusicPlayer().setPlayTimestamp(command.getTimestamp());
                    setMessage("Returned to previous track successfully. The current track is "
                                + currentEpisode.getName() + ".");
                }
                break;

            default:
                break;
        }
    }

    /**
     * Sets the message for the previous track operation.
     *
     * @param message  The message to be set.
     */
    public void setMessage(final String message) {
        this.message = message;
    }
}

