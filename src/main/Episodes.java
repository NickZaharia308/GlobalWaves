package main;

public class Episodes {
    private String name;
    private int duration;
    private String description;
    private int remainingTime;

    public Episodes(String name, int duration, String description) {
        this.name = name;
        this.duration = duration;
        this.description = description;
        this.remainingTime = duration;
    }

    // Copy constructor
    public Episodes(Episodes otherEpisode) {
        this.name = otherEpisode.name;
        this.duration = otherEpisode.duration;
        this.description = otherEpisode.description;
        this.remainingTime = otherEpisode.remainingTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }
}
