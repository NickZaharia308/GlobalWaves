package main;

import java.util.ArrayList;

public class ShowPlaylists extends Command {
    ArrayList<Playlists> playlists;
    public void returnShowPlaylists (Command command, Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        ArrayList<Playlists> playlists = library.getPlaylists();
        ArrayList<Playlists> userPlaylist = new ArrayList<>();

        for (Playlists playlist : playlists) {
            if (playlist.getOwner().equals(command.getUsername())) {
                userPlaylist.add(playlist);
            }
        }
        setPlaylists(userPlaylist);
    }

    public ArrayList<Playlists> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(ArrayList<Playlists> playlists) {
        this.playlists = playlists;
    }
}
