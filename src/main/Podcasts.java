package main;

import java.util.ArrayList;

public class Podcasts {
    private String name;
    private String owner;
    private ArrayList<Episodes> episodes;

    public Podcasts(String name, String owner, ArrayList<Episodes> episodes) {
        this.name = name;
        this.owner = owner;
        this.episodes = episodes;
    }

    // Copy constructor
    public Podcasts(Podcasts otherPodcast) {
        this.name = otherPodcast.name;
        this.owner = otherPodcast.owner;

        // Create a new ArrayList and copy the episodes
        this.episodes = new ArrayList<>();
        for (Episodes episode : otherPodcast.episodes) {
            this.episodes.add(new Episodes(episode));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public ArrayList<Episodes> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(ArrayList<Episodes> episodes) {
        this.episodes = episodes;
    }
}
