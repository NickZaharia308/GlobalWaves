package commands.users.host;

import commands.Command;
import commands.page.PageSubject;
import commands.users.Status;
import lombok.Getter;
import main.Library;
import user.entities.Host;
import user.entities.Users;
import user.entities.audio.files.Podcasts;

import java.util.ArrayList;

/**
 * The {@code RemovePodcast} class represents a command to remove a podcast by a host.
 * It extends the {@link Command} class and is used to process and execute commands
 * related to removing podcasts from the host's collection. The class includes methods to validate
 * the input command, check if the podcast to be deleted is loaded by any user, and remove
 * the podcast from the host's collection.
 * It notifies the users that are on the host's page.
 */
@Getter
public class RemovePodcast extends Command {
    private String message;

    /**
     * Processes the input command to remove a podcast by a host user and notifies observers.
     *
     * @param command The command containing information about the user and
     * the operation to be performed.
     * @param library The main library containing information about users and entities.
     */
    public void returnRemovePodcast(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());
        super.setName(command.getName());

        Users user = new Users();
        user = user.getUser(library.getUsers(), this.getUsername());

        if (user == null) {
            setMessage("The username " + this.getUsername() + " doesn't exist.");
            return;
        }

        if (user.getUserType() != Users.UserType.HOST) {
            setMessage(this.getUsername() + " is not a host.");
            return;
        }

        Host host = (Host) user;
        Podcasts podcastToDelete = null;
        for (Podcasts podcast : host.getPodcasts()) {
            if (podcast.getName().equals(this.getName())) {
                podcastToDelete = podcast;
                break;
            }
        }

        if (podcastToDelete == null) {
            setMessage(this.getUsername() + " doesn't have a podcast with the given name.");
            return;
        }

        if (isPodcastLoaded(library, command, podcastToDelete)) {
            setMessage(this.getUsername() + " can't delete this podcast.");
            return;
        }

        host.getPodcasts().remove(podcastToDelete);
        library.getPodcasts().remove(podcastToDelete);

        // Notify the observers
        PageSubject pageSubject = new PageSubject();
        pageSubject.notifyObservers(host.getUsername());

        setMessage(this.getUsername() + " deleted the podcast successfully.");
    }

    /**
     * Checks if the podcast to be deleted is loaded by any user.
     *
     * @param library         The main library containing information about users and entities.
     * @param command         The command containing information about the user and the operation
     *                        to be performed.
     * @param podcastToDelete The podcast to be deleted.
     * @return {@code true} if the podcast is loaded, {@code false} otherwise.
     */
    private boolean isPodcastLoaded(final Library library, final Command command,
                                    final Podcasts podcastToDelete) {
        ArrayList<Users> allUsers = library.getUsers();

        for (Users user : allUsers) {
            Status status = new Status();
            command.setUsername(user.getUsername());
            status.returnStatus(command, library);

            // If the user has a podcast loaded and the podcast is the podcast we want to delete
            if (user.isSomethingLoaded() && user.getTrackType() == Users.Track.PODCAST
                    && user.getMusicPlayer().getPodcast().getName()
                    .equals(podcastToDelete.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the message indicating the result of the remove podcast operation.
     *
     * @param message The message to be set.
     */
    public void setMessage(final String message) {
        this.message = message;
    }
}

