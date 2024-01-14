package user.entities;

public class UserFactory {
    public static Users createUser(String username, int age, String city, String userType) {
        if (userType.equals("artist")) {
            Artist artist = new Artist(username, age, city);
            artist.setUserType(Users.UserType.ARTIST);
            return artist;
        } else if (userType.equals("host")) {
            Host host = new Host(username, age, city);
            host.setUserType(Users.UserType.HOST);
            return host;
        } else {
            return new Users(username, age, city);
        }
    }
}
