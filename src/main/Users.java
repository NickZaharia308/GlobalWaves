package main;

import java.util.LinkedList;

public class Users {
    private String username;
    private int age;
    private String city;
    private LinkedList<String> searchResults = null;
    private int noOfSearchResults = -1;

    public Users(String username, int age, String city) {
        this.username = username;
        this.age = age;
        this.city = city;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LinkedList<String> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(LinkedList<String> searchResults) {
        this.searchResults = searchResults;
    }

    public int getNoOfSearchResults() {
        return noOfSearchResults;
    }

    public void setNoOfSearchResults(int noOfSearchResults) {
        this.noOfSearchResults = noOfSearchResults;
    }
}
