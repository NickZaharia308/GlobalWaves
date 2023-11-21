package main;

import checker.Checker;
import checker.CheckerConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;



/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    static final String LIBRARY_PATH = CheckerConstants.TESTS_PATH + "library/library.json";

    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.getName().startsWith("library")) {
                continue;
            }

            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePathInput for input file
     * @param filePathOutput for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePathInput,
                              final String filePathOutput) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        LibraryInput library = objectMapper.readValue(new File(LIBRARY_PATH), LibraryInput.class);

        ArrayNode outputs = objectMapper.createArrayNode();

        // TODO add your implementation
//        if (!filePathInput.equals("test04_like_create_addRemove_error.json"))
//            return;
        Playlists.noOfPlaylists = 0;

        Library myLibrary = new Library();

        // Getting the 'UserInput' ArrayList and transferring it in my ArrayList
        ArrayList<UserInput> userInputArrayList = library.getUsers();
        ArrayList<Users> users = new ArrayList<>();
        for (UserInput input : userInputArrayList) {
            Users user = new Users(input.getUsername(), input.getAge(), input.getCity());
            users.add(user);
        }
        // Setting the Library's users
        myLibrary.setUsers(users);

        // Getting the 'SongInput' ArrayList and transferring it in my ArrayList
        ArrayList<SongInput> songInputArrayList = library.getSongs();
        ArrayList<Songs> songs = new ArrayList<>();
        for (SongInput input: songInputArrayList) {
            Songs song = new Songs(input.getName(), input.getDuration(), input.getAlbum(), input.getTags(),
                                    input.getLyrics(), input.getGenre(), input.getReleaseYear(), input.getArtist());
            songs.add(song);
        }
        // Setting the Library's songs
        myLibrary.setSongs(songs);

        // Getting the 'PodcastInput' ArrayList and transferring it in my ArrayList
        ArrayList<PodcastInput> podcastInputArrayList = library.getPodcasts();
        ArrayList<Podcasts> podcasts = new ArrayList<>();
        for (PodcastInput input: podcastInputArrayList) {
            // Getting the 'EpisodeInput' ArrayList and transferring it in my ArrayList
            ArrayList<EpisodeInput> episodeInputArrayList = input.getEpisodes();
            ArrayList<Episodes> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : episodeInputArrayList) {
                Episodes episode = new Episodes(episodeInput.getName(), episodeInput.getDuration(),
                        episodeInput.getDescription());
                episodes.add(episode);
            }

            // Creating the podcast itself, now that I have the array of episodes
            Podcasts podcast = new Podcasts(input.getName(), input.getOwner(), episodes);
            podcasts.add(podcast);
        }
        // Setting the Library's podcasts
        myLibrary.setPodcasts(podcasts);

        ArrayList<Playlists> playlists = new ArrayList<>();
        myLibrary.setPlaylists(playlists);

        LinkedList<Command> commands = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH + filePathInput), new TypeReference<LinkedList<Command>>(){});

        // Iterating through commands
        for (Command command: commands) {

            // If the given command is "search"
            if (Objects.equals(command.getCommand(), "search")) {
                Search search = new Search();
                search.returnSearch(command, myLibrary);

                ObjectNode resultNode = objectMapper.createObjectNode();
                resultNode.put("command", search.getCommand());
                resultNode.put("user", search.getUsername());
                resultNode.put("timestamp", search.getTimestamp());
                resultNode.put("message", search.getMessage());

                ArrayNode resultsArrayNode = resultNode.putArray("results");
                LinkedList<String> results = search.getResults();

                for (String result : results) {
                    resultsArrayNode.add(result);
                }
                resultNode.set("results", resultsArrayNode);

                outputs.add(resultNode);

            } else if (Objects.equals(command.getCommand(), "select")) {
                Select select = new Select();
                select.returnSelect(command, myLibrary);
                ObjectNode resultNode = objectMapper.createObjectNode();
                resultNode.put("command", select.getCommand());
                resultNode.put("user", select.getUsername());
                resultNode.put("timestamp", select.getTimestamp());
                resultNode.put("message", select.getMessage());
                outputs.add(resultNode);

            } else if (Objects.equals(command.getCommand(), "load")) {
                Load load = new Load();
                load.returnLoad(command, myLibrary);
                ObjectNode resultNode = objectMapper.createObjectNode();
                resultNode.put("command", load.getCommand());
                resultNode.put("user", load.getUsername());
                resultNode.put("timestamp", load.getTimestamp());
                resultNode.put("message", load.getMessage());
                outputs.add(resultNode);
            } else if (Objects.equals(command.getCommand(), "status")) {
                Status status = new Status();
                status.returnStatus(command, myLibrary);
                ObjectNode resultNode = objectMapper.createObjectNode();
                resultNode.put("command", status.getCommand());
                resultNode.put("user", status.getUsername());
                resultNode.put("timestamp", status.getTimestamp());

                ObjectNode statsNode = objectMapper.createObjectNode();
                statsNode.put("name", status.getTrackName());
                statsNode.put("remainedTime", status.getRemainedTime());
                if (status.isRepeat()) {
                    statsNode.put("repeat", "Repeat");
                } else {
                    statsNode.put("repeat", "No Repeat");
                }
                statsNode.put("shuffle", status.isShuffle());
                statsNode.put("paused", status.isPaused());
                resultNode.set("stats", statsNode);
                outputs.add(resultNode);

            } else if (Objects.equals(command.getCommand(), "playPause")) {
                PlayPause playPause = new PlayPause();
                playPause.returnPlayPause(command, myLibrary);
                ObjectNode resultNode = objectMapper.createObjectNode();
                resultNode.put("command", playPause.getCommand());
                resultNode.put("user", playPause.getUsername());
                resultNode.put("timestamp", playPause.getTimestamp());
                resultNode.put("message", playPause.getMessage());
                outputs.add(resultNode);
            } else if (Objects.equals(command.getCommand(), "createPlaylist")) {
                CreatePlaylist createPlaylist = new CreatePlaylist();
                createPlaylist.returnCreatePlaylist(command, myLibrary);
                ObjectNode resultNode = objectMapper.createObjectNode();
                resultNode.put("command", createPlaylist.getCommand());
                resultNode.put("user", createPlaylist.getUsername());
                resultNode.put("timestamp", createPlaylist.getTimestamp());
                resultNode.put("message", createPlaylist.getMessage());
                outputs.add(resultNode);
            } else if (Objects.equals(command.getCommand(), "addRemoveInPlaylist")) {
                AddRemoveInPlaylist addRemoveInPlaylist = new AddRemoveInPlaylist();
                addRemoveInPlaylist.returnAddRemoveInPlaylist(command, myLibrary);
                ObjectNode resultNode = objectMapper.createObjectNode();
                resultNode.put("command", addRemoveInPlaylist.getCommand());
                resultNode.put("user", addRemoveInPlaylist.getUsername());
                resultNode.put("timestamp", addRemoveInPlaylist.getTimestamp());
                resultNode.put("message", addRemoveInPlaylist.getMessage());
                outputs.add(resultNode);
            } else if (Objects.equals(command.getCommand(), "like")) {
                Like like = new Like();
                like.returnLike(command, myLibrary);
                ObjectNode resultNode = objectMapper.createObjectNode();
                resultNode.put("command", like.getCommand());
                resultNode.put("user", like.getUsername());
                resultNode.put("timestamp", like.getTimestamp());
                resultNode.put("message", like.getMessage());
                outputs.add(resultNode);
            } else if (Objects.equals(command.getCommand(), "showPlaylists")) {
                ShowPlaylists showPlaylists = new ShowPlaylists();
                showPlaylists.returnShowPlaylists(command, myLibrary);

                ObjectNode resultNode = objectMapper.createObjectNode();
                resultNode.put("command", showPlaylists.getCommand());
                resultNode.put("user", showPlaylists.getUsername());
                resultNode.put("timestamp", showPlaylists.getTimestamp());

                ArrayNode resultsArrayNode = resultNode.putArray("result");
                ArrayList<Playlists> userPlaylists = showPlaylists.getPlaylists();

                for (Playlists playlist : userPlaylists) {
                    ObjectNode playlistNode = objectMapper.createObjectNode();
                    playlistNode.put("name", playlist.getName());

                    ArrayNode songsArrayNode = playlistNode.putArray("songs");

                    for (Songs song : playlist.getSongs()) {
                        if (song != null) {
                            songsArrayNode.add(song.getName());
                        }
                    }

                    playlistNode.put("visibility", playlist.getVisibility());
                    playlistNode.put("followers", playlist.getFollowers());

                    resultsArrayNode.add(playlistNode);
                }

                resultNode.set("result", resultsArrayNode);
                outputs.add(resultNode);

            } else if (Objects.equals(command.getCommand(), "showPreferredSongs")) {
                ShowPreferredSongs showPreferredSongs = new ShowPreferredSongs();
                showPreferredSongs.returnShowPreferredSongs(command, myLibrary);

                ObjectNode resultNode = objectMapper.createObjectNode();
                resultNode.put("command", showPreferredSongs.getCommand());
                resultNode.put("user", showPreferredSongs.getUsername());
                resultNode.put("timestamp", showPreferredSongs.getTimestamp());

                ArrayNode resultsArrayNode = resultNode.putArray("result");
                ArrayList<Songs> likedSongs = showPreferredSongs.getLikedSongs();

                for (Songs song : likedSongs) {
                    resultsArrayNode.add(song.getName());
                }

                resultNode.set("result", resultsArrayNode);
                outputs.add(resultNode);
            }
        }

        // Code ends here

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePathOutput), outputs);
    }
}
