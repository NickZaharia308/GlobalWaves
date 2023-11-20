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

            }
        }

        // Code ends here

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePathOutput), outputs);
    }
}
