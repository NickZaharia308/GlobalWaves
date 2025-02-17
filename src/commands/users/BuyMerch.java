package commands.users;

import commands.Command;
import lombok.Getter;
import lombok.Setter;
import main.Library;
import user.entities.Artist;
import user.entities.Users;
import user.entities.specialEntities.Merch;
import user.entities.specialEntities.PageMenu;

import java.util.ArrayList;

/**
 * BuyMerch class is used by a user to buy merchandise from an artist's page.
 */
@Setter
@Getter
public class BuyMerch extends Command {
    private String message;

    /**
     * Performs a buy merch operation based on the provided command.
     * A user can by merch from an artist's page.
     *
     * @param command The buy merch command containing user-specific information and timestamp.
     * @param library The library containing songs, playlists, podcasts, and user information.
     */
    public void returnBuyMerch(final Command command, final Library library) {
        super.setCommand(command.getCommand());
        super.setUsername(command.getUsername());
        super.setTimestamp(command.getTimestamp());

        Users user = new Users();
        user = user.getUser(library.getUsers(), command.getUsername());

        if (user == null) {
            setMessage("The username " + user.getUsername() + " doesn't exist.");
            return;
        }

        if (user.getPageMenu().getCurrentPage() != PageMenu.Page.ARTISTPAGE) {
            setMessage("Cannot buy merch from this page.");
            return;
        }

        Artist artist = (Artist) user.getUser(library.getUsers(),
                                                user.getPageMenu().getPageOwnerName());
        Merch merchToBuy = getMerchandiseFromArtist(artist.getMerchandise(), command.getName());
        if (merchToBuy == null) {
            setMessage("The merch " + command.getName() + " doesn't exist.");
            return;
        }

        user.getBoughtMerchandise().add(merchToBuy.getName());
        setMessage(user.getUsername() + " has added new merch successfully.");

        artist.setMerchRevenue(artist.getMerchRevenue() + merchToBuy.getPrice());
    }

    /**
     * Gets the merchandise from the artist's page.
     *
     * @param merchArrayList The list of merchandise from the artist's page.
     * @param merchName      The name of the merchandise to retrieve.
     * @return The merchandise with the specified name, or null if not found.
     */
    private Merch getMerchandiseFromArtist(final ArrayList<Merch> merchArrayList,
                                           final String merchName) {
        return merchArrayList.stream()
                .filter(merch -> merch.getName().equals(merchName))
                .findFirst()
                .orElse(null);
    }
}
