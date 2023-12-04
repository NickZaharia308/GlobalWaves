package userEntities.specialEntities;

import lombok.Getter;

@Getter
public class Merch {
    private String name;
    private String description;
    private int price;

    public Merch (String name, String description, int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
