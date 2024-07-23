package backend.src;

public class User {
    private String username;
    private String password; // Stored as a hash
    private String email;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeEmail(String email) {
        this.email = email;
    }

    public void save() {
        // Save the user to the database
    }

    public void delete() {
        // Delete the user from the database
    }

    public static User findByUsername(String username) {
        // Find the user in the database by username
        return null;
    }

    public static User findByEmail(String email) {
        // Find the user in the database by email
        return null;
    }
}