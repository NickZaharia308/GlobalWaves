package main;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.LinkedList;

public class Select extends Command {
    private String message;

    public void returnSelect (Command command, Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        int itemNumber = command.getItemNumber();

        ArrayList<Users> users = library.getUsers();
        for (Users user : users) {
            if (user.getUsername().equals(command.getUsername())) {
                if (user.getNoOfSearchResults() == -1) {
                    message = "Please conduct a search before making a selection.";
                } else if (itemNumber > user.getNoOfSearchResults()) {
                    message = "The selected ID is too high.";
                } else {
                    LinkedList<String> searchResults = user.getSearchResults();

                    // Creating the musicPlayer if it doesn't exist
                    MusicPlayer musicPlayer = user.getMusicPlayer();
                    if (user.getMusicPlayer() == null) {
                        musicPlayer = new MusicPlayer();
                    }

                    // If the track is a song
                    if (user.getTrackType() == Users.Track.SONG) {
                        String selectedSong = searchResults.get(itemNumber - 1);

                        // Finding the selected song and adding it to user
                        ArrayList<Songs> songs = library.getSongs();
                        for (Songs song : songs) {
                            if (song.getName().equals(selectedSong)) {
                                musicPlayer.setSong(song);
                                user.setMusicPlayer(musicPlayer);
                                break;
                            }
                        }

                        message = "Successfully selected " + user.getMusicPlayer().getSong().getName() + ".";
                    } else if (user.getTrackType() == Users.Track.PLAYLIST) {
                        String selectedPlaylist = searchResults.get(itemNumber - 1);

                        // Finding the selected playlist and adding it to user
                        ArrayList<Playlists> playlists = library.getPlaylists();
                        for (Playlists playlist : playlists) {
                            if (playlist.getName().equals(selectedPlaylist)) {
                                musicPlayer.setPlaylist(playlist);
                                user.setMusicPlayer(musicPlayer);
                                break;
                            }
                        }
                        if (user.getMusicPlayer().getPlaylist() == null)
                            return;
                        message = "Successfully selected " + user.getMusicPlayer().getPlaylist().getName() + ".";
                    } else if (user.getTrackType() == Users.Track.PODCAST) {
                        String selectedPodcast = searchResults.get(itemNumber - 1);

                        // Finding the selected podcast and adding it to user
                        if (musicPlayer.getPodcasts() == null)
                            musicPlayer.setPodcasts(library.getPodcasts());

                        for (Podcasts podcast : musicPlayer.getPodcasts()) {
                            if (podcast.getName().equals(selectedPodcast)) {
                                musicPlayer.setPodcast(podcast);
                                user.setMusicPlayer(musicPlayer);
                                break;
                            }
                        }

                        message = "Successfully selected " + user.getMusicPlayer().getPodcast().getName() + ".";
                    }
                    user.setSearchResults(null);
                    user.setNoOfSearchResults(-1);
                    user.setSomethingSelected(true);
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
