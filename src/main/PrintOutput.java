package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import commands.admin.AddUser;
import commands.admin.DeleteUser;
import commands.admin.EndProgram;
import commands.admin.ShowAlbums;
import commands.admin.ShowPodcasts;
import commands.page.ChangePage;
import commands.page.PrintCurrentPage;
import commands.page.command.NextPage;
import commands.page.command.PreviousPage;
import commands.searchBar.Load;
import commands.searchBar.Search;
import commands.searchBar.Select;
import commands.statistics.GetAllUsers;
import commands.statistics.GetOnlineUsers;
import commands.statistics.GetTop5Albums;
import commands.statistics.GetTop5Artists;
import commands.statistics.GetTop5Playlists;
import commands.statistics.GetTop5Songs;
import commands.statistics.Wrapped;
import commands.users.AdBreak;
import commands.users.AddRemoveInPlaylist;
import commands.users.Backward;
import commands.users.BuyMerch;
import commands.users.BuyPremium;
import commands.users.CancelPremium;
import commands.users.Forward;
import commands.users.Like;
import commands.users.LoadRecommendations;
import commands.users.Next;
import commands.users.PlayPause;
import commands.users.Prev;
import commands.users.Repeat;
import commands.users.SeeMerch;
import commands.users.ShowPreferredSongs;
import commands.users.Shuffle;
import commands.users.Status;
import commands.users.SwitchConnectionStatus;
import commands.users.UpdateRecommendations;
import commands.users.artist.AddAlbum;
import commands.users.artist.AddEvent;
import commands.users.artist.AddMerch;
import commands.users.artist.RemoveAlbum;
import commands.users.artist.RemoveEvent;
import commands.users.host.AddAnnouncement;
import commands.users.host.AddPodcast;
import commands.users.host.RemoveAnnouncement;
import commands.users.host.RemovePodcast;
import commands.users.notifications.GetNotifications;
import commands.users.notifications.Subscribe;
import commands.users.playlists.CreatePlaylist;
import commands.users.playlists.FollowPlaylist;
import commands.users.playlists.ShowPlaylists;
import commands.users.playlists.SwitchVisibility;
import user.entities.Artist;
import user.entities.Users;
import user.entities.audio.files.Album;
import user.entities.audio.files.Episodes;
import user.entities.audio.files.Playlists;
import user.entities.audio.files.Podcasts;
import user.entities.audio.files.Songs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a utility class for printing output based on different commands.
 */
public final class PrintOutput {

    private PrintOutput() {
    }
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
        } else if (Objects.equals(command.getCommand(), "addEvent")) {
            AddEvent addEvent = new AddEvent();
            addEvent.returnAddEvent(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", addEvent.getCommand());
            resultNode.put("user", addEvent.getUsername());
            resultNode.put("timestamp", addEvent.getTimestamp());
            resultNode.put("message", addEvent.getMessage());
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "addMerch")) {
            AddMerch addMerch = new AddMerch();
            addMerch.returnAddMerch(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", addMerch.getCommand());
            resultNode.put("user", addMerch.getUsername());
            resultNode.put("timestamp", addMerch.getTimestamp());
            resultNode.put("message", addMerch.getMessage());
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "getAllUsers")) {
            GetAllUsers getAllUsers = new GetAllUsers();
            ArrayList<Users> allUsers = getAllUsers.returnGetAllUsers(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", getAllUsers.getCommand());
            resultNode.put("timestamp", getAllUsers.getTimestamp());

            ArrayNode resultsArrayNode = resultNode.putArray("result");

            for (Users user : allUsers) {
                resultsArrayNode.add(user.getUsername());
            }

            resultNode.set("result", resultsArrayNode);
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "deleteUser")) {
            DeleteUser deleteUser = new DeleteUser();
            deleteUser.returnDeleteUser(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", deleteUser.getCommand());
            resultNode.put("user", deleteUser.getUsername());
            resultNode.put("timestamp", deleteUser.getTimestamp());
            resultNode.put("message", deleteUser.getMessage());
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "addPodcast")) {
            AddPodcast addPodcast = new AddPodcast();
            addPodcast.returnAddPodcast(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", addPodcast.getCommand());
            resultNode.put("user", addPodcast.getUsername());
            resultNode.put("timestamp", addPodcast.getTimestamp());
            resultNode.put("message", addPodcast.getMessage());
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "addAnnouncement")) {
            AddAnnouncement addAnnouncement = new AddAnnouncement();
            addAnnouncement.returnAddAnnouncement(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", addAnnouncement.getCommand());
            resultNode.put("user", addAnnouncement.getUsername());
            resultNode.put("timestamp", addAnnouncement.getTimestamp());
            resultNode.put("message", addAnnouncement.getMessage());
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "removeAnnouncement")) {
            RemoveAnnouncement removeAnnouncement = new RemoveAnnouncement();
            removeAnnouncement.returnRemoveAnnouncement(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", removeAnnouncement.getCommand());
            resultNode.put("user", removeAnnouncement.getUsername());
            resultNode.put("timestamp", removeAnnouncement.getTimestamp());
            resultNode.put("message", removeAnnouncement.getMessage());
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "showPodcasts")) {
            ShowPodcasts showPodcasts = new ShowPodcasts();
            ArrayList<Podcasts> podcasts = showPodcasts.returnShowPodcasts(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", showPodcasts.getCommand());
            resultNode.put("user", showPodcasts.getUsername());
            resultNode.put("timestamp", showPodcasts.getTimestamp());

            ArrayNode resultsArrayNode = resultNode.putArray("result");
            for (Podcasts podcast : podcasts) {
                ObjectNode podcastNode = objectMapper.createObjectNode();
                podcastNode.put("name", podcast.getName());

                ArrayNode episodesArrayNode = podcastNode.putArray("episodes");

                for (Episodes episode : podcast.getEpisodes()) {
                    if (episode != null) {
                        episodesArrayNode.add(episode.getName());
                    }
                }

                resultsArrayNode.add(podcastNode);
            }

            resultNode.set("result", resultsArrayNode);
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "removeAlbum")) {
            RemoveAlbum removeAlbum = new RemoveAlbum();
            removeAlbum.returnRemoveAlbum(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", removeAlbum.getCommand());
            resultNode.put("user", removeAlbum.getUsername());
            resultNode.put("timestamp", removeAlbum.getTimestamp());
            resultNode.put("message", removeAlbum.getMessage());
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "changePage")) {
            ChangePage changePage = new ChangePage();
            changePage.returnChangePage(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", changePage.getCommand());
            resultNode.put("user", changePage.getUsername());
            resultNode.put("timestamp", changePage.getTimestamp());
            resultNode.put("message", changePage.getMessage());
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "removePodcast")) {
            RemovePodcast removePodcast = new RemovePodcast();
            removePodcast.returnRemovePodcast(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", removePodcast.getCommand());
            resultNode.put("user", removePodcast.getUsername());
            resultNode.put("timestamp", removePodcast.getTimestamp());
            resultNode.put("message", removePodcast.getMessage());
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "removeEvent")) {
            RemoveEvent removeEvent = new RemoveEvent();
            removeEvent.returnRemoveEvent(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", removeEvent.getCommand());
            resultNode.put("user", removeEvent.getUsername());
            resultNode.put("timestamp", removeEvent.getTimestamp());
            resultNode.put("message", removeEvent.getMessage());
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "getTop5Albums")) {
            GetTop5Albums getTop5Albums = new GetTop5Albums();
            getTop5Albums.returnGetTop5Albums(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", getTop5Albums.getCommand());
            resultNode.put("timestamp", getTop5Albums.getTimestamp());

            ArrayNode resultsArrayNode = resultNode.putArray("result");
            ArrayList<Album> top5Albums = getTop5Albums.getTopAlbums();

            for (Album album : top5Albums) {
                resultsArrayNode.add(album.getName());
            }

            resultNode.set("result", resultsArrayNode);
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "getTop5Artists")) {
            GetTop5Artists getTop5Artists = new GetTop5Artists();
            getTop5Artists.returnGetTop5Artists(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", getTop5Artists.getCommand());
            resultNode.put("timestamp", getTop5Artists.getTimestamp());

            ArrayNode resultsArrayNode = resultNode.putArray("result");
            ArrayList<Artist> top5Artists = getTop5Artists.getTopArtist();

            for (Artist artist : top5Artists) {
                resultsArrayNode.add(artist.getUsername());
            }
            resultNode.set("result", resultsArrayNode);
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "wrapped")) {
            Wrapped wrapped = new Wrapped();
            wrapped.returnWrapped(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", wrapped.getCommand());
            resultNode.put("user", wrapped.getUsername());
            resultNode.put("timestamp", wrapped.getTimestamp());

            if (wrapped.getMessage() != null) {
                resultNode.put("message", wrapped.getMessage());
                outputs.add(resultNode);
                return;
            }

            ObjectNode resultObjectNode = objectMapper.createObjectNode();

            if (wrapped.getUserType() == Users.UserType.NORMAL) {
                ObjectNode topArtistsNode = objectMapper.createObjectNode();
                for (Map.Entry<String, Integer> entry : wrapped.getTopArtists()) {
                    topArtistsNode.put(entry.getKey(), entry.getValue());
                }
                resultObjectNode.set("topArtists", topArtistsNode);
            }

            if (wrapped.getUserType() == Users.UserType.NORMAL) {
                ObjectNode topGenresNode = objectMapper.createObjectNode();
                for (Map.Entry<String, Integer> entry : wrapped.getTopGenres()) {
                    topGenresNode.put(entry.getKey(), entry.getValue());
                }
                resultObjectNode.set("topGenres", topGenresNode);
            }

            if (wrapped.getUserType() == Users.UserType.NORMAL
                    || wrapped.getUserType() == Users.UserType.ARTIST) {
                ObjectNode topSongsNode = objectMapper.createObjectNode();
                for (Map.Entry<String, Integer> entry : wrapped.getTopSongs()) {
                    topSongsNode.put(entry.getKey(), entry.getValue());
                }
                resultObjectNode.set("topSongs", topSongsNode);
            }

            if (wrapped.getUserType() == Users.UserType.NORMAL
                    || wrapped.getUserType() == Users.UserType.ARTIST) {
                ObjectNode topAlbums = objectMapper.createObjectNode();
                for (Map.Entry<String, Integer> entry : wrapped.getTopAlbums()) {
                    topAlbums.put(entry.getKey(), entry.getValue());
                }
                resultObjectNode.set("topAlbums", topAlbums);
            }

            if (wrapped.getUserType() == Users.UserType.NORMAL
                    || wrapped.getUserType() == Users.UserType.HOST) {
                ObjectNode topEpisodes = objectMapper.createObjectNode();
                for (Map.Entry<String, Integer> entry : wrapped.getTopEpisodes()) {
                    topEpisodes.put(entry.getKey(), entry.getValue());
                }
                resultObjectNode.set("topEpisodes", topEpisodes);
            }

            if (wrapped.getUserType() == Users.UserType.ARTIST) {
                ArrayNode topFansArray = objectMapper.createArrayNode();
                for (Map.Entry<String, Integer> entry : wrapped.getTopFans()) {
                    topFansArray.add(entry.getKey());
                }
                resultObjectNode.set("topFans", topFansArray);
            }

            if (wrapped.getUserType() == Users.UserType.ARTIST
                    || wrapped.getUserType() == Users.UserType.HOST) {
                int count = 0;
                for (Map.Entry<String, Boolean> entry : wrapped.getListeners()) {
                    if (entry.getValue()) {
                        count++;
                    }
                }
                resultObjectNode.put("listeners", count);
            }

            resultNode.set("result", resultObjectNode);
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "endProgram")) {
            EndProgram endProgram = new EndProgram();
            endProgram.returnEndProgram(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", endProgram.getCommand());

            ArrayNode resultsArrayNode = resultNode.putArray("result");
            ObjectNode artistsNode = objectMapper.createObjectNode();


            for (Artist artist : endProgram.getPlatformArtists()) {
                if (artist.hasTrueValueInListeners() || artist.getMerchRevenue() > 0) {
                    ObjectNode artistInfoNode = objectMapper.createObjectNode();
                    artistInfoNode.put("merchRevenue", artist.getMerchRevenue());
                    artistInfoNode.put("mostProfitableSong", artist.getMostProfitableSong());
                    artistInfoNode.put("ranking", artist.getRanking());
                    artistInfoNode.put("songRevenue", artist.getSongRevenue());

                    artistsNode.set(artist.getUsername(), artistInfoNode);
                }
            }

            resultNode.set("result", artistsNode);
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "buyPremium")) {
            BuyPremium buyPremium = new BuyPremium();
            buyPremium.returnBuyPremium(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", buyPremium.getCommand());
            resultNode.put("user", buyPremium.getUsername());
            resultNode.put("timestamp", buyPremium.getTimestamp());
            resultNode.put("message", buyPremium.getMessage());
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "cancelPremium")) {
            CancelPremium cancelPremium = new CancelPremium();
            cancelPremium.returnCancelPremium(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", cancelPremium.getCommand());
            resultNode.put("user", cancelPremium.getUsername());
            resultNode.put("timestamp", cancelPremium.getTimestamp());
            resultNode.put("message", cancelPremium.getMessage());
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "adBreak")) {
            AdBreak adBreak = new AdBreak();
            adBreak.returnAdBreak(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", adBreak.getCommand());
            resultNode.put("user", adBreak.getUsername());
            resultNode.put("timestamp", adBreak.getTimestamp());
            resultNode.put("message", adBreak.getMessage());
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "subscribe")) {
            Subscribe subscribe = new Subscribe();
            subscribe.returnSubscribe(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", subscribe.getCommand());
            resultNode.put("user", subscribe.getUsername());
            resultNode.put("timestamp", subscribe.getTimestamp());
            resultNode.put("message", subscribe.getMessage());
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "getNotifications")) {
            GetNotifications getNotifications = new GetNotifications();
            getNotifications.returnGetNotification(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", getNotifications.getCommand());
            resultNode.put("user", getNotifications.getUsername());
            resultNode.put("timestamp", getNotifications.getTimestamp());

            ArrayNode notificationsArray = resultNode.putArray("notifications");

            while (!getNotifications.getNotifications().isEmpty()) {
                String name = getNotifications.getNotifications().poll();
                String description = getNotifications.getNotifications().poll();

                ObjectNode notificationNode = objectMapper.createObjectNode();
                notificationNode.put("name", name);
                notificationNode.put("description", description);
                notificationsArray.add(notificationNode);
            }

            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "buyMerch")) {
            BuyMerch buyMerch = new BuyMerch();
            buyMerch.returnBuyMerch(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", buyMerch.getCommand());
            resultNode.put("user", buyMerch.getUsername());
            resultNode.put("timestamp", buyMerch.getTimestamp());
            resultNode.put("message", buyMerch.getMessage());
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "seeMerch")) {
            SeeMerch seeMerch = new SeeMerch();
            seeMerch.returnSeeMerch(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", seeMerch.getCommand());
            resultNode.put("user", seeMerch.getUsername());
            resultNode.put("timestamp", seeMerch.getTimestamp());

            if (!seeMerch.getMessage().equals("")) {
                resultNode.put("message", seeMerch.getMessage());
            } else {
                ArrayNode merchArray = resultNode.putArray("result");
                for (String merch : seeMerch.getMerch()) {
                    merchArray.add(merch);
                }
            }

            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "updateRecommendations")) {
            UpdateRecommendations updateRecommendations = new UpdateRecommendations();
            updateRecommendations.returnUpdateRecommendations(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", updateRecommendations.getCommand());
            resultNode.put("user", updateRecommendations.getUsername());
            resultNode.put("timestamp", updateRecommendations.getTimestamp());
            resultNode.put("message", updateRecommendations.getMessage());
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "previousPage")) {
            PreviousPage previousPage = new PreviousPage();
            previousPage.returnPreviousPage(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", previousPage.getCommand());
            resultNode.put("user", previousPage.getUsername());
            resultNode.put("timestamp", previousPage.getTimestamp());
            resultNode.put("message", previousPage.getMessage());
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "nextPage")) {
            NextPage nextPage = new NextPage();
            nextPage.returnNextPage(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", nextPage.getCommand());
            resultNode.put("user", nextPage.getUsername());
            resultNode.put("timestamp", nextPage.getTimestamp());
            resultNode.put("message", nextPage.getMessage());
            outputs.add(resultNode);
        } else if (Objects.equals(command.getCommand(), "loadRecommendations")) {
            LoadRecommendations loadRecommendations = new LoadRecommendations();
            loadRecommendations.returnLoadRecommendations(command, myLibrary);

            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("command", loadRecommendations.getCommand());
            resultNode.put("user", loadRecommendations.getUsername());
            resultNode.put("timestamp", loadRecommendations.getTimestamp());
            resultNode.put("message", loadRecommendations.getMessage());
            outputs.add(resultNode);
        }
    }
}
