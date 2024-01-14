package commands.users;

import commands.Command;
import commands.statistics.Wrapped;
import lombok.Getter;
import lombok.Setter;
import main.Library;
import user.entities.Artist;
import user.entities.Users;
import user.entities.audio.files.Songs;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

@Getter
@Setter
public class AdBreak extends Command {
    private String message;
    public void returnAdBreak(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());

        if (user == null) {
            setMessage("The username " + command.getUsername() + " doesn't exist.");
            return;
        }

        Wrapped wrapped = new Wrapped();
        wrapped.returnWrapped(command, library);

        if (user.getMusicPlayer() == null || user.getMusicPlayer().isPaused()) {
            setMessage(command.getUsername() + " is not playing any music.");
            return;
        }

        Songs adBreak = getAdd(library);
        if (adBreak == null) {
            return;
        }

        addToFrontOfQueue(user.getMusicPlayer().getTrackQueue(), adBreak);
        adBreak.setAdPrice(command.getPrice());


        setMessage("Ad inserted successfully.");
    }

    private void addToFrontOfQueue(Queue<Songs> trackQueue, Songs ad) {
        // Create a temporary queue to hold the current playing song, the ad, and existing songs
        Queue<Songs> tempQueue = new LinkedList<>();

        // Add the new song to the temporary queue
        tempQueue.offer(ad);

        // Add all existing songs to the temporary queue
        tempQueue.addAll(trackQueue);

        // Clear the original queue and add all songs from the temporary queue
        trackQueue.clear();
        trackQueue.addAll(tempQueue);
    }

    private Songs getAdd(final Library library) {
        for (Songs song : library.getSongs()) {
            if (song.getName().equals("Ad Break")) {
                return song;
            }
        }
        return null;
    }


}
