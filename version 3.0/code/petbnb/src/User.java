public class User {
    public String email;
    public String password;
    public String role;
    public String name;
    public String lastname;
    public String age;
    public String location;
    public String pets;
    public String preferences;
    public String rating;
    public String profilePicPath;

    public User(String email, String password, String role, String name, String lastname,
                String age, String location, String pets, String preferences, String rating, String profilePicPath) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.name = name;
        this.lastname = lastname;
        this.age = age;
        this.location = location;
        this.pets = pets;
        this.preferences = preferences;
        this.rating = rating;
        this.profilePicPath = profilePicPath;
    }
}