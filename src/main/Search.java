package main;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Search extends Command {
    private String message;
    private LinkedList<String> results;
    private int noOfResults = 0;
    public void returnSearch(Command command, Library library) {

        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());
        this.results = new LinkedList<>();

        // Getting all the filters in a JsonNode
        JsonNode filters = command.getFilters();


        if (Objects.equals(command.getType(), "song")) {
            ArrayList<Songs> songs = library.getSongs();
            for (Songs song : songs) {
                boolean matchesAllFilters = true;

                if (filters.has("name")) {
                    String substring = filters.get("name").asText();
                    matchesAllFilters &= song.getName().startsWith(substring);
                }

                if (filters.has("album")) {
                    String album = filters.get("album").asText();
                    matchesAllFilters &= Objects.equals(song.getAlbum(), album);
                }

                if (filters.has("tags")) {

                    // Getting all the tags in a "JsonNode"
                    JsonNode tagsNode = filters.get("tags");

                    // Iterating over the elements of the array and adding them to an ArrayList
                    ArrayList<String> tags = new ArrayList<>();
                    for (JsonNode tagNode : tagsNode) {
                        tags.add(tagNode.asText());
                    }
                    matchesAllFilters &= song.getTags().containsAll(tags);
                }

                if (filters.has("lyrics")) {
                    String lyrics = filters.get("lyrics").asText();
                    matchesAllFilters &= song.getLyrics().toLowerCase().contains(lyrics.toLowerCase());
                }

                if (filters.has("genre")) {
                    String genre = filters.get("genre").asText();
                    matchesAllFilters &= song.getGenre().equalsIgnoreCase(genre);
                }

                if (filters.has("releaseYear")) {
                    String releaseYear = filters.get("releaseYear").asText();

                    // Defining a regex pattern to extract the operator and year
                    Pattern pattern = Pattern.compile("([<>])(\\d+)");

                    // Using a Matcher to find matches in the string
                    Matcher matcher = pattern.matcher(releaseYear);

                    if (matcher.find()) {

                        // Group 1 contains the operator, Group 2 contains the year
                        String operator = matcher.group(1);
                        int comparisonYear = Integer.parseInt(matcher.group(2));
                        int givenYear = song.getReleaseYear();

                        // Comparing the given year based on the operator
                        matchesAllFilters &= compareYears(givenYear, operator, comparisonYear);
                    }
                }

                if (filters.has("artist")) {
                    String artist = filters.get("artist").asText();
                    matchesAllFilters &= Objects.equals(song.getArtist(), artist);
                }

                if (matchesAllFilters && noOfResults < 5) {
                    noOfResults++;
                    this.results.add(song.getName());
                } else if (noOfResults == 5) {
                    break;
                }
            }
        } else if (Objects.equals(command.getType(), "podcast")) {
            ArrayList<Podcasts> podcasts = library.getPodcasts();
            for (Podcasts podcast : podcasts) {
                boolean matchesAllFilters = true;

                if (filters.has("name")) {
                    String substring = filters.get("name").asText();
                    matchesAllFilters &= podcast.getName().startsWith(substring);
                }

                if (filters.has("owner")) {
                    String owner = filters.get("owner").asText();
                    matchesAllFilters &= Objects.equals(podcast.getOwner(), owner);
                }

                if (matchesAllFilters && noOfResults < 5) {
                    noOfResults++;
                    this.results.add(podcast.getName());
                } else if (noOfResults == 5) {
                    break;
                }
            }
        } else if (Objects.equals(command.getType(), "playlist")) {
            ArrayList<Playlists> playlists = library.getPlaylists();
            for (Playlists playlist : playlists) {
                boolean matchesAllFilters = true;

                if (filters.has("name")) {
                    String substring = filters.get("name").asText();
                    matchesAllFilters &= playlist.getName().startsWith(substring);
                }

                if (filters.has("owner")) {
                    String owner = filters.get("owner").asText();
                    matchesAllFilters &= Objects.equals(playlist.getOwner(), owner);
                } else {
                    // If the searched playlist is public
                    matchesAllFilters &= playlist.getVisibility().equals("public");
                }

                if (matchesAllFilters && noOfResults < 5) {
                    noOfResults++;
                    this.results.add(playlist.getName());
                } else if (noOfResults == 5) {
                    break;
                }
            }
        }

        this.message = "Search returned " + noOfResults + " results";

        // Finding the user and setting the 'noOfResults' and the 'results' list
        ArrayList<Users> users = library.getUsers();
        for (Users user : users) {
            if (user.getUsername().equals(command.getUsername())) {
                user.setNoOfSearchResults(noOfResults);
                user.setSearchResults(results);

                // Checking if the search interrupts an episode
                if (user.getMusicPlayer() != null && user.getTrackType() == Users.Track.PODCAST
                    &&  user.getMusicPlayer().getEpisode() != null) {
                    // Computing the remaining time for a track
                    int playTimestamp = user.getMusicPlayer().getPlayTimestamp();
                    int timestamp = command.getTimestamp();
                    int leftTime = user.getMusicPlayer().getEpisode().getRemainingTime() + playTimestamp - timestamp;
                    user.getMusicPlayer().getEpisode().setRemainingTime(leftTime);
                }

                // Canceling the MusicPlayer (loader)
                user.setSomethingLoaded(false);

                // Setting the type of search (song, playlist, podcast)
                if (Objects.equals(command.getType(), "song")) {
                    user.setTrackType(Users.Track.SONG);
                } else if (Objects.equals(command.getType(), "podcast")) {
                    user.setTrackType(Users.Track.PODCAST);
                } else {
                    user.setTrackType(Users.Track.PLAYLIST);
                }
            }
        }

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LinkedList<String> getResults() {
        return results;
    }

    public void setResults(LinkedList<String> results) {
        this.results = results;
    }

    public int getnoOfResults() {
        return noOfResults;
    }

    public void setnoOfResults(int noOfResults) {
        this.noOfResults = noOfResults;
    }

    // Helper method to compare years based on the operator
    private static boolean compareYears(int givenYear, String operator, int comparisonYear) {
        switch (operator) {
            case ">":
                return givenYear > comparisonYear;
            case "<":
                return givenYear < comparisonYear;
            default:
                // Handle the case when the operator is not > or <
                return false;
        }
    }
}
