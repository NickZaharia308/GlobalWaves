package userEntities.specialEntities;

import lombok.Getter;

@Getter
public class Announcement {
    private String name;
    private String description;

    public Announcement(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
