package commands.admin;

import commands.Command;
import main.Library;
import user.entities.audio.files.Album;

import java.util.ArrayList;

public class EndProgram extends Command {

    public void returnEndProgram(final Command command, final Library library) {
        super.setCommand(command.getCommand());
    }
}
