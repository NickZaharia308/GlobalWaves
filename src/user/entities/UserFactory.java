package user.entities;

/**
 * The UserFactory class is a factory for creating users.
 */
public final class UserFactory {

    /**
     * Private constructor to prevent instantiation.
     */
    private UserFactory() {
    }

    /**
     * Creates a new user with the specified username, age, city, and user type.
     * The user type can be "artist", "host", or "normal".
     *
     * @param username The username of the user.
     * @param age      The age of the user.
     * @param city     The city where the user resides.
     * @param userType The type of user to create.
     * @return A new user with the specified username, age, city, and user type.
     */
    public static Users createUser(final String username, final int age, final String city,
                                   final String userType) {
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
