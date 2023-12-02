package userEntities;

public class Host extends Users {


    /**
     * Constructs a new Artist object with the specified username, age, and city.
     *
     * @param username The username of the user.
     * @param age      The age of the user.
     * @param city     The city where the user resides.
     */
    public Host(final String username, final int age, final String city) {
        super.username = username;
        super.age = age;
        super.city = city;
    }
}
