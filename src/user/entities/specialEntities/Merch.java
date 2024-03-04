package user.entities.specialEntities;

import lombok.Getter;
import lombok.Setter;

/**
 * The Merch class represents merchandise with a name, description, and price.
 * Merch is created by an artist.
 */
@Getter
@Setter
public class Merch {
    private String name;
    private String description;
    private int price;

    /**
     * Constructs a new Merch object with the specified name, description, and price.
     *
     * @param name        The name of the merchandise.
     * @param description The description of the merchandise.
     * @param price       The price of the merchandise.
     */
    public Merch(final String name, final String description, final int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
