package backend.src;
import java.sql.Statement;

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

    public void insertUser(Statement stmt) {
        // Insert the user to the database
        String sqlInsert = "INSERT INTO \"User\" (username, password, email) " +
            "VALUES ('" + username + "', '" + password + "', '" + email + "');";
        
            try {
                stmt.executeUpdate(sqlInsert);
                System.out.println("User inserted successfully");
        } catch (Exception e) {
            System.out.println("Error inserting user: " + e.getMessage());
        }
    }

    public void delete(Statement stmt) {
        // Delete the user from the database
        String sqlDelete = "DELETE FROM \"User\" WHERE username = '" + username + "';";
        try {
            stmt.executeUpdate(sqlDelete);
            System.out.println("User deleted successfully");
        } catch (Exception e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
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