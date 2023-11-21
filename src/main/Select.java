package main;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.LinkedList;

public class Select extends Command {
    private String message;

    public void returnSelect (Command command, Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        int itemNumber = command.getItemNumber();

        ArrayList<Users> users = library.getUsers();
        for (Users user : users) {
            if (user.getUsername().equals(command.getUsername())) {
                if (user.getNoOfSearchResults() == -1) {
                    message = "Please conduct a search before making a selection.";
                } else if (itemNumber > user.getNoOfSearchResults()) {
                    message = "The selected ID is too high.";
                } else {
                    LinkedList<String> searchResults = user.getSearchResults();
                    String selectedSong = searchResults.get(itemNumber - 1);

                    // Finding the selected song and adding it to user
                    ArrayList<Songs> songs = library.getSongs();
                    for (Songs song : songs) {
                        if (song.getName().equals(selectedSong)) {
                            user.setSelectedSong(song);
                            break;
                        }
                    }
                    if (user.getSelectedSong() == null)
                        return;
                    message = "Successfully selected " + user.getSelectedSong().getName() + ".";
                    user.setSearchResults(null);
                    user.setNoOfSearchResults(-1);
                    user.setSomethingSelected(true);
                }
                break;
            }
        }

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
