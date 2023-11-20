package main;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.LinkedList;

public class Select extends Command{
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
                    message = "Successfully selected " + searchResults.get(itemNumber - 1) + ".";
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
