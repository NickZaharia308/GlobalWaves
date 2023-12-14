package user.entities.specialEntities;

import lombok.Getter;

/**
 * The Merch class represents merchandise with a name, description, and price.
 * Merch is created by an artist.
 */
@Getter
public class Merch {

    /**
     * The name of the merchandise.
     */
    private String name;

    /**
     * The description of the merchandise.
     */
    private String description;

    /**
     * The price of the merchandise.
     */
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

    /**
     * Sets the name of the merchandise.
     *
     * @param name The name to set.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Sets the description of the merchandise.
     *
     * @param description The description to set.
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Sets the price of the merchandise.
     *
     * @param price The price to set.
     */
    public void setPrice(final int price) {
        this.price = price;
    }
}
