package main;

public class Users {
    private String username;
    private int age;
    private String city;

    public Users(String username, int age, String city) {
        this.username = username;
        this.age = age;
        this.city = city;
    }
    public Object[] getParams() {
        return new Object[]{getUsername(), getAge(), getCity()};
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


}
