package commands.searchBar;

import com.fasterxml.jackson.databind.JsonNode;
import commands.Command;
import commands.page.observer.PageSubject;
import commands.statistics.Wrapped;
import lombok.Getter;
import main.Library;
import user.entities.Artist;
import user.entities.Users;
import user.entities.audio.files.Album;
import user.entities.audio.files.Playlists;
import user.entities.audio.files.Podcasts;
import user.entities.audio.files.Songs;
import user.entities.specialEntities.PageMenu;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Search class represents a search operation based on user commands in the library.
 * It extends the Command class.
 */
@Getter
public class Search extends Command {
    private String message;
    private LinkedList<String> results;
    private int noOfResults = 0;
    private final int maxSearches = 5;
    private ArrayList<Album> albumResult = new ArrayList<>();

    /**
     * Performs a search based on the provided command and updates the search results and message.
     * The search is limited to a maximum number of results defined by {@code maxSearches}.
     *
     * @param command The search command containing filters and search parameters.
     * @param library The library containing songs, podcasts, albums and playlists to search within.
     */
    public final void returnSearch(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());
        this.results = new LinkedList<>();

        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());


        if (!user.isOnline()) {
            setMessage(user.getUsername() + " is offline.");
            return;
        }

        // Getting all the filters in a JsonNode
        JsonNode filters = command.getFilters();

        if (Objects.equals(command.getType(), "song")) {
            searchSongs(library, filters);
        } else if (Objects.equals(command.getType(), "podcast")) {
            searchPodcasts(library, filters);
        } else if (Objects.equals(command.getType(), "playlist")) {
            searchPlaylists(library, filters, command);
        } else if (Objects.equals(command.getType(), "artist")) {
            searchArtist(library, filters);
        } else if (Objects.equals(command.getType(), "album")) {
            searchAlbum(library, filters);
        } else if (Objects.equals(command.getType(), "host")) {
            searchHost(library, filters);
        }

        updateResultsAndMessage(library, command);
    }

    private void searchSongs(final Library library, final JsonNode filters) {
        ArrayList<Songs> songs = library.getSongs();
        for (Songs song : songs) {
            boolean matchesAllFilters = true;
            if (filters.has("name")) {
                String substring = filters.get("name").asText().toLowerCase();
                matchesAllFilters &= song.getName().toLowerCase().startsWith(substring);
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
                matchesAllFilters &= song.getLyrics().toLowerCase().
                        contains(lyrics.toLowerCase());
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
            if (matchesAllFilters && noOfResults < maxSearches) {
                noOfResults++;
                this.results.add(song.getName());
            } else if (noOfResults == maxSearches) {
                break;
            }
        }
    }

    private void searchPodcasts(final Library library, final JsonNode filters) {
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
            if (matchesAllFilters && noOfResults < maxSearches) {
                noOfResults++;
                this.results.add(podcast.getName());
            } else if (noOfResults == maxSearches) {
                break;
            }
        }
    }
    private void searchPlaylists(final Library library, final JsonNode filters,
                                 final Command command) {
        ArrayList<Playlists> playlists = library.getPlaylists();
        for (Playlists playlist : playlists) {
            boolean matchesAllFilters = true;
            if (filters.has("name")) {
                String substring = filters.get("name").asText();
                matchesAllFilters &= playlist.getName().toLowerCase().startsWith(substring);
            }
            if (filters.has("owner")) {
                String owner = filters.get("owner").asText();
                matchesAllFilters &= Objects.equals(playlist.getOwner(), owner);
                if (!playlist.getOwner().equals(command.getUsername())) {
                    matchesAllFilters &= playlist.getVisibility().equals("public");
                }
            } else {
                // If the searched playlist is public
                matchesAllFilters &= playlist.getVisibility().equals("public");
            }
            if (matchesAllFilters && noOfResults < maxSearches) {
                noOfResults++;
                this.results.add(playlist.getName());
            } else if (noOfResults == maxSearches) {
                break;
            }
        }
    }

    private void searchArtist(final Library library, final JsonNode filters) {
        ArrayList<Users> users = library.getUsers();
        for (Users user : users) {
            boolean matchesAllFilters = true;

            // Check if the name matches
            if (filters.has("name")) {
                String substring = filters.get("name").asText();
                matchesAllFilters &= user.getUsername().startsWith(substring);
            }

            // Check if it is an artist
            matchesAllFilters &= user.getUserType() == Users.UserType.ARTIST;

            if (matchesAllFilters && noOfResults < maxSearches) {
                noOfResults++;
                this.results.add(user.getUsername());
            } else if (noOfResults == maxSearches) {
                break;
            }
        }
    }

    private void searchAlbum(final Library library, final JsonNode filters) {
        ArrayList<Album> albums = library.getAlbums();
        for (Album album : albums) {
            boolean matchesAllFilters = true;
            if (filters.has("name")) {
                String substring = filters.get("name").asText();
                matchesAllFilters &= album.getName().startsWith(substring);
            }
            if (filters.has("owner")) {
                String owner = filters.get("owner").asText();
                matchesAllFilters &= album.getOwner().startsWith(owner);
            }

            if (filters.has("description")) {
                String description = filters.get("description").asText();
                matchesAllFilters &= album.getDescription().startsWith(description);
            }

            if (matchesAllFilters) {
                noOfResults++;
                this.albumResult.add(album);
            }
        }
        sortAlbums(library, this.albumResult);
        this.results = extractAlbumNames(this.albumResult);
        if (noOfResults > 5) {
            noOfResults = 5;
        }
    }

    private void searchHost(final Library library, final JsonNode filters) {
        ArrayList<Users> users = library.getUsers();
        for (Users user : users) {
            boolean matchesAllFilters = true;

            // Check if the name matches
            if (filters.has("name")) {
                String substring = filters.get("name").asText();
                matchesAllFilters &= user.getUsername().startsWith(substring);
            }

            // Check if it is an artist
            matchesAllFilters &= user.getUserType() == Users.UserType.HOST;

            if (matchesAllFilters && noOfResults < maxSearches) {
                noOfResults++;
                this.results.add(user.getUsername());
            } else if (noOfResults == maxSearches) {
                break;
            }
        }
    }

    /**
     * Updates the search results and message based on the performed search.
     * This method is called after the search is completed to set the message,
     * results, and additional user information.
     *
     * @param library The library containing songs, podcasts, albums and playlists.
     * @param command The original search command.
     */
    private void updateResultsAndMessage(final Library library, final Command command) {
        this.message = "Search returned " + noOfResults + " results";

        // Finding the user and setting the 'noOfResults' and the 'results' list
        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());

        user.setNoOfSearchResults(this.noOfResults);
        user.setSearchResults(this.results);
        if (Objects.equals(command.getType(), "album")) {
            user.setAlbumResults(albumResult);
        }

        // Update the Wrapped for user
        Wrapped wrapped = new Wrapped();
        wrapped.returnWrapped(command, library);

        // Checking if the search interrupts an episode
        if (user.getMusicPlayer() != null && user.getTrackType() == Users.Track.PODCAST
                &&  user.getMusicPlayer().getEpisode() != null) {
            // Computing the remaining time for a track
            int playTimestamp = user.getMusicPlayer().getPlayTimestamp();
            int timestamp = command.getTimestamp();
            int leftTime = user.getMusicPlayer().getEpisode().getRemainingTime()
                            + playTimestamp - timestamp;
            user.getMusicPlayer().setPlayTimestamp(command.getTimestamp());
            user.getMusicPlayer().getEpisode().setRemainingTime(leftTime);
        }


        // Canceling the MusicPlayer (loader)
        user.setSomethingLoaded(false);

        // Remove the User (Observer) from the host / artist (Subject) if the user
        // searched another page and had previously a host page or an artist page
        if ((user.getPageMenu().getCurrentPage() == PageMenu.Page.ARTISTPAGE
            || user.getPageMenu().getCurrentPage() == PageMenu.Page.HOSTPAGE)
            && (Objects.equals(command.getType(), "artist")
                    || Objects.equals(command.getType(), "host"))) {
            PageSubject pageSubject = new PageSubject();
            pageSubject.removeObserver(user.getPageMenu().getPageOwnerName(), user);
        }

        // Setting the type of search (song, playlist, podcast)
        if (Objects.equals(command.getType(), "song")) {
            user.setTrackType(Users.Track.SONG);
        } else if (Objects.equals(command.getType(), "podcast")) {
            user.setTrackType(Users.Track.PODCAST);
        } else if (Objects.equals(command.getType(), "playlist")) {
            user.setTrackType(Users.Track.PLAYLIST);
        } else if (Objects.equals(command.getType(), "album")) {
            user.setTrackType(Users.Track.ALBUM);
        } else if (Objects.equals(command.getType(), "artist")) {
            // No page found, keep the previous one
            if (noOfResults == 0) {
                return;
            }
            user.getPageMenu().setCurrentPage(PageMenu.Page.ARTISTPAGE);
            user.setPageSearched(true);
        } else if (Objects.equals(command.getType(), "host")) {
            // No page found, keep the previous one
            if (noOfResults == 0) {
                return;
            }
            user.getPageMenu().setCurrentPage(PageMenu.Page.HOSTPAGE);
            user.setPageSearched(true);
        }
    }

    /**
     * Sets the search message.
     *
     * @param message The search message to be set.
     */
    public void setMessage(final String message) {
        this.message = message;
    }


    /**
     * Sets the search results.
     *
     * @param results The list of search results to be set.
     */
    public void setResults(final LinkedList<String> results) {
        this.results = results;
    }


    /**
     * Helper method to compare years based on the operator.
     *
     * @param givenYear       The year to be compared.
     * @param operator         The comparison operator ("<" or ">").
     * @param comparisonYear   The year to compare against.
     * @return                 True if the comparison is successful, false otherwise.
     */
    private static boolean compareYears(final int givenYear,
                                        final String operator,
                                        final int comparisonYear) {
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

    public static void sortAlbums(final Library myLibrary, List<Album> albums) {
        Comparator<Album> albumComparator = Comparator
                .comparing((Album album) -> {
                    Artist owner = getArtistByUsername(myLibrary, album.getOwner());
                    return owner != null ? owner.getAddOnPlatformOrder() : Long.MAX_VALUE;
                })
                .thenComparing(Album::getAddOrder);

        albums.sort(albumComparator);
    }

    private static Artist getArtistByUsername(final Library library, final String username) {

        Users user = new Users();
        user = user.getUser(library.getUsers(), username);

        return (Artist) user;
    }

    private LinkedList<String> extractAlbumNames(List<Album> sortedAlbums) {
        LinkedList<String> results = new LinkedList<>();
        int maxAlbums = 0;
        for (Album album : sortedAlbums) {
            if (maxAlbums < this.maxSearches) {
                results.add(album.getName());
            } else {
                break;
            }
            maxAlbums++;
        }
        return results;
    }
}
