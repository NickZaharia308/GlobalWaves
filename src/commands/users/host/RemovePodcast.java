package commands.users.host;

import commands.Command;
import commands.page.Subject;
import commands.users.Status;
import lombok.Getter;
import main.Library;
import userEntities.Host;
import userEntities.Users;
import userEntities.audio.Podcasts;

import java.util.ArrayList;

@Getter
public class RemovePodcast extends Command {
    String message;

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

        // Notify the observers
        Subject subject = new Subject();
        subject.notifyObservers(host.getUsername());

        setMessage(this.getUsername() +" deleted the podcast successfully.");
    }

    private boolean isPodcastLoaded(Library library, Command command, Podcasts podcastToDelete) {
        ArrayList<Users> allUsers = library.getUsers();

        for (Users user: allUsers) {
            Status status = new Status();
            command.setUsername(user.getUsername());
            status.returnStatus(command, library);

            // If the user has a podcast loaded and the podcast is the podcast we want to delete
            if (user.isSomethingLoaded() && user.getTrackType() == Users.Track.PODCAST
                    && user.getMusicPlayer().getPodcast().getName().equals(podcastToDelete.getName())) {
                return true;
            }
        }
        return false;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
