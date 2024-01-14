package main;

import checker.Checker;
import checker.CheckerConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.core.type.TypeReference;
import commands.Command;
import fileio.input.EpisodeInput;
import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;
import user.entities.Users;
import user.entities.audio.files.Episodes;
import user.entities.audio.files.Playlists;
import user.entities.audio.files.Podcasts;
import user.entities.audio.files.Songs;

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
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        LibraryInput library = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH
                                                               + "library/library.json"),
                                                               LibraryInput.class);
//        CommandInput[] commands = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH
//                                                                  + filePath1),
//                                                                  CommandInput[].class);
//        if (!filePath1.equals("test06_etapa3_monetization_all.json")) {
//            return;
//        }

        ArrayNode outputs = objectMapper.createArrayNode();

        Playlists.setNoOfPlaylists(0);

        Library myLibrary = Library.getInstance();

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
            Songs song = new Songs(input.getName(), input.getDuration(),
                    input.getAlbum(), input.getTags(),
                    input.getLyrics(), input.getGenre(),
                    input.getReleaseYear(), input.getArtist());
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

        // Setting the Library's playlists
        ArrayList<Playlists> playlists = new ArrayList<>();
        myLibrary.setPlaylists(playlists);

        LinkedList<Command> commands = objectMapper.readValue(
                new File(CheckerConstants.TESTS_PATH
                        + filePath1), new TypeReference<LinkedList<Command>>() { });

        // Iterating through commands
        for (Command command: commands) {
            PrintOutput.printOutput(outputs, objectMapper, command, myLibrary);
        }
        Command endProgram = new Command();
        endProgram.setCommand("endProgram");
        PrintOutput.printOutput(outputs, objectMapper, endProgram, myLibrary);

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), outputs);

        myLibrary.reset();
    }
}
