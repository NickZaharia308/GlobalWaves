package commands.users;

import commands.Command;
import commands.statistics.Wrapped;
import lombok.Getter;
import lombok.Setter;
import main.Library;
import user.entities.Users;
import user.entities.audio.files.Songs;

import java.util.LinkedList;
import java.util.Queue;

/**
 * AdBreak class is used to insert an ad break in the queue of a user's (if the user is not
 * premium) music player.
 * Note that the ad can be overwritten by the load command.
 */
@Getter
@Setter
public class AdBreak extends Command {
    private String message;

    /**
     * Inserts an ad break in the queue of a user's music player.
     * @param command the command to be executed
     * @param library the main library
     */
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

        adBreak.setAdPrice(command.getPrice());
        addToFrontOfQueue(user.getMusicPlayer().getTrackQueue(), adBreak);

        setMessage("Ad inserted successfully.");
    }

    /**
     * Adds the ad to the front of the queue.
     * @param trackQueue the queue of the user's music player
     * @param ad the ad to be added
     */
    private void addToFrontOfQueue(final Queue<Songs> trackQueue, final Songs ad) {
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

    /**
     * Gets the ad break from the library.
     * @param library the main library
     * @return the ad break
     */
    private Songs getAdd(final Library library) {
        for (Songs song : library.getSongs()) {
            if (song.getName().equals("Ad Break")) {
                return song;
            }
        }
        return null;
    }


}
