package commands.users;

import commands.Command;
import commands.searchBar.Load;
import lombok.Getter;
import lombok.Setter;
import main.Library;
import user.entities.Users;

@Getter
@Setter
public class LoadRecommendations extends Command {
    private String message;

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
