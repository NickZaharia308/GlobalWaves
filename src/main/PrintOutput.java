package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import commands.admin.AddUser;
import commands.admin.ShowAlbums;
import commands.page.PrintCurrentPage;
import commands.searchBar.Load;
import commands.searchBar.Search;
import commands.searchBar.Select;
import commands.users.*;
import commands.users.artist.AddAlbum;
import commands.users.playlists.CreatePlaylist;
import commands.users.playlists.FollowPlaylist;
import commands.users.playlists.ShowPlaylists;
import commands.users.playlists.SwitchVisibility;
import commands.statistics.GetOnlineUsers;
import commands.statistics.GetTop5Playlists;
import commands.statistics.GetTop5Songs;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import userEntities.Users;
import userEntities.audio.Album;
import userEntities.audio.Playlists;
import userEntities.audio.Songs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Represents a utility class for printing output based on different commands.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PrintOutput {

    /**
     * Prints output based on the provided command.
     *
     * @param outputs       The ArrayNode containing output information.
     * @param objectMapper  The ObjectMapper for creating JSON nodes.
     * @param command       The command for which output needs to be printed.
     * @param myLibrary     The library containing playlists, songs, and user information.
     */
    public static void printOutput(final ArrayNode outputs, final ObjectMapper objectMapper,
                                   final Command command, final Library myLibrary) {
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
            statsNode.put("repeat", status.getRepeatMessage());
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
            ArrayList<Playlists> userPlaylists = showPlaylists.getRetrievedPlaylists();

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
        } else if (Objects.equals(command.getCommand(), "repeat")) {
            Repeat repeat = new Repeat();
            repeat.returnRepeat(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", repeat.getCommand());
            resultNode.put("user", repeat.getUsername());
            resultNode.put("timestamp", repeat.getTimestamp());
            resultNode.put("message", repeat.getMessage());
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "shuffle")) {
            Shuffle shuffle = new Shuffle();
            shuffle.returnShuffle(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", shuffle.getCommand());
            resultNode.put("user", shuffle.getUsername());
            resultNode.put("timestamp", shuffle.getTimestamp());
            resultNode.put("message", shuffle.getMessage());
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "next")) {
            Next next = new Next();
            next.returnNext(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", next.getCommand());
            resultNode.put("user", next.getUsername());
            resultNode.put("timestamp", next.getTimestamp());
            resultNode.put("message", next.getMessage());
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "prev")) {
            Prev prev = new Prev();
            prev.returnPrev(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", prev.getCommand());
            resultNode.put("user", prev.getUsername());
            resultNode.put("timestamp", prev.getTimestamp());
            resultNode.put("message", prev.getMessage());
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "forward")) {
            Forward forward = new Forward();
            forward.returnForward(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", forward.getCommand());
            resultNode.put("user", forward.getUsername());
            resultNode.put("timestamp", forward.getTimestamp());
            resultNode.put("message", forward.getMessage());
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "backward")) {
            Backward backward = new Backward();
            backward.returnBackward(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", backward.getCommand());
            resultNode.put("user", backward.getUsername());
            resultNode.put("timestamp", backward.getTimestamp());
            resultNode.put("message", backward.getMessage());
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "follow")) {
            FollowPlaylist followPlaylist = new FollowPlaylist();
            followPlaylist.returnFollowPlaylist(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", followPlaylist.getCommand());
            resultNode.put("user", followPlaylist.getUsername());
            resultNode.put("timestamp", followPlaylist.getTimestamp());
            resultNode.put("message", followPlaylist.getMessage());
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "switchVisibility")) {
            SwitchVisibility switchVisibility = new SwitchVisibility();
            switchVisibility.returnSwitchVisibility(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", switchVisibility.getCommand());
            resultNode.put("user", switchVisibility.getUsername());
            resultNode.put("timestamp", switchVisibility.getTimestamp());
            resultNode.put("message", switchVisibility.getMessage());
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "getTop5Playlists")) {
            GetTop5Playlists getTop5Playlists = new GetTop5Playlists();
            getTop5Playlists.returnGetTop5Playlists(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", getTop5Playlists.getCommand());
            resultNode.put("timestamp", getTop5Playlists.getTimestamp());

            ArrayNode resultsArrayNode = resultNode.putArray("result");
            ArrayList<Playlists> top5Playlists = getTop5Playlists.getPlaylists();

            for (Playlists playlist : top5Playlists) {
                resultsArrayNode.add(playlist.getName());
            }

            resultNode.set("result", resultsArrayNode);
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "getTop5Songs")) {
            GetTop5Songs getTop5Songs = new GetTop5Songs();
            getTop5Songs.returnGetTop5Songs(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", getTop5Songs.getCommand());
            resultNode.put("timestamp", getTop5Songs.getTimestamp());

            ArrayNode resultsArrayNode = resultNode.putArray("result");
            ArrayList<Songs> top5Songs = getTop5Songs.getTopSongs();

            for (Songs song : top5Songs) {
                resultsArrayNode.add(song.getName());
            }

            resultNode.set("result", resultsArrayNode);
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "switchConnectionStatus")) {
            SwitchConnectionStatus switchConnectionStatus = new SwitchConnectionStatus();
            switchConnectionStatus.returnSwitchConnectionStatus(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", switchConnectionStatus.getCommand());
            resultNode.put("user", switchConnectionStatus.getUsername());
            resultNode.put("timestamp", switchConnectionStatus.getTimestamp());
            resultNode.put("message", switchConnectionStatus.getMessage());
            outputs.add(resultNode);

        } else if (Objects.equals(command.getCommand(), "getOnlineUsers")) {
            GetOnlineUsers getOnlineUsers = new GetOnlineUsers();
            ArrayList<Users> onlineUsers = getOnlineUsers.returnGetOnlineUsers(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", getOnlineUsers.getCommand());
            resultNode.put("timestamp", getOnlineUsers.getTimestamp());

            ArrayNode resultsArrayNode = resultNode.putArray("result");

            for (Users user : onlineUsers) {
                resultsArrayNode.add(user.getUsername());
            }

            resultNode.set("result", resultsArrayNode);
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "addUser")) {
            AddUser addUser = new AddUser();
            addUser.returnAddUser(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", addUser.getCommand());
            resultNode.put("user", addUser.getUsername());
            resultNode.put("timestamp", addUser.getTimestamp());
            resultNode.put("message", addUser.getMessage());
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "addAlbum")) {
            AddAlbum addAlbum = new AddAlbum();
            addAlbum.returnAddAlbum(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", addAlbum.getCommand());
            resultNode.put("user", addAlbum.getUsername());
            resultNode.put("timestamp", addAlbum.getTimestamp());
            resultNode.put("message", addAlbum.getMessage());
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "showAlbums")) {
            ShowAlbums showAlbums = new ShowAlbums();
            ArrayList<Album> albums = showAlbums.returnShowAlbum(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", showAlbums.getCommand());
            resultNode.put("user", showAlbums.getUsername());
            resultNode.put("timestamp", showAlbums.getTimestamp());

            ArrayNode resultsArrayNode = resultNode.putArray("result");
            for (Album album : albums) {
                ObjectNode albumNode = objectMapper.createObjectNode();
                albumNode.put("name", album.getName());

                ArrayNode songsArrayNode = albumNode.putArray("songs");

                for (Songs song : album.getSongs()) {
                    if (song != null) {
                        songsArrayNode.add(song.getName());
                    }
                }

                resultsArrayNode.add(albumNode);
            }

            resultNode.set("result", resultsArrayNode);
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "printCurrentPage")) {
            PrintCurrentPage printCurrentPage = new PrintCurrentPage();
            printCurrentPage.returnPrintCurrentPage(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", printCurrentPage.getCommand());
            resultNode.put("user", printCurrentPage.getUsername());
            resultNode.put("timestamp", printCurrentPage.getTimestamp());
            resultNode.put("message", printCurrentPage.getMessage());
            outputs.add(resultNode);
        }
    }
}
