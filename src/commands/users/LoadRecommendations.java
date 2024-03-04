package commands.users;

import commands.Command;
import commands.searchBar.Load;
import lombok.Getter;
import lombok.Setter;
import main.Library;
import user.entities.Users;

/**
 * LoadRecommendations class is used to load the last recommendation for a user.
 * The last recommendation can be a song or a playlist.
 * Note: The class does not compute the recommendation, it only loads it. The class responsible for
 * computing the recommendation is the UpdateRecommendations class.
 *
 */
@Getter
@Setter
public class LoadRecommendations extends Command {
    private String message;

    /**
     * Loads the last recommendation for a user.
     * @param command the command to be executed
     * @param library the main library
     */
    public void returnLoadRecommendations(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());

        if (!user.isOnline()) {
            setMessage(user.getUsername() + " is offline.");
            return;
        }

        if (user.getLastRecommendationType() == Users.Track.SONG) {
            user.getMusicPlayer().setSong(user.getLastRecommendedSong());
            user.setTrackType(Users.Track.SONG);
        } else if (user.getLastRecommendationType() == Users.Track.PLAYLIST) {
            user.getMusicPlayer().setPlaylist(user.getLastRecommendedPlaylist());
            user.setTrackType(Users.Track.PLAYLIST);
        }
        user.setSomethingSelected(true);
        Load load = new Load();
        load.returnLoad(command, library);

        setMessage("Playback loaded successfully.");
    }

}
