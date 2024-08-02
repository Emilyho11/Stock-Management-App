package backend_old.src;
import java.sql.ResultSet;
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

    public void insertUser(Statement stmt) {
        // Insert the user to the database
        String sqlInsert = "INSERT INTO users (username, password, email) " +
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
        String sqlDelete = "DELETE FROM users WHERE username = '" + username + "';";
        try {
            stmt.executeUpdate(sqlDelete);
            System.out.println("User deleted successfully");
        } catch (Exception e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }

    public static User findByUsername(Statement stmt, ResultSet rs, String username) {
        // Find the user in the database by username
        String sqlFind = "SELECT * FROM users WHERE username = '" + username + "';";
        try {
            rs = stmt.executeQuery(sqlFind);
            if (rs.next()) {
                username = rs.getString("username");
                String email = rs.getString("email");
                String password = rs.getString("password");
                return new User(username, password, email);
            }
        } catch (Exception e) {
            System.out.println("Error finding user: " + e.getMessage());
        }
        return null;
    }

    public static void printAllUsers(Statement stmt, ResultSet rs) {
        // Print all users in the database
        String getAllUsers = "SELECT username, email FROM users;";
        try {
            rs = stmt.executeQuery(getAllUsers);
            System.out.println("Table User contains the following tuples:\nusername \temail");
            while (rs.next()) {
                String username = rs.getString("username");
                String email = rs.getString("email");
                System.out.println(username + ", " + email);
            }
        } catch (Exception e) {
            System.out.println("Error printing all users: " + e.getMessage());
        }
    }
    public void changePassword(Statement stmt, String password) {
        this.password = password;
        String sqlUpdate = "UPDATE users SET password = '" + password + "' WHERE username = '" + username + "';";
        try {
            stmt.executeUpdate(sqlUpdate);
            System.out.println("Password updated successfully");
        } catch (Exception e) {
            System.out.println("Error updating password: " + e.getMessage());
        }
    }

    public void changeEmail(Statement stmt, String email) {
        this.email = email;
        String sqlUpdate = "UPDATE users SET email = '" + email + "' WHERE username = '" + username + "';";
        if (findByUsername(stmt, null, email) != null) {
            System.out.println("A user with this email already exists");
            return;
        }
        try {
            stmt.executeUpdate(sqlUpdate);
            System.out.println("Email updated successfully");
        } catch (Exception e) {
            System.out.println("Error updating email: " + e.getMessage());
        }
    }

    public void changeUsername(Statement stmt, String newUsername) {
        // Check if username already exists. If it does, print that a user with this username already exists
        if (findByUsername(stmt, null, newUsername) != null) {
            System.out.println("A user with this username already exists");
            return;
        }
    
        // SQL query to update the username
        String sqlUpdate = "UPDATE users SET username = '" + newUsername + "' WHERE username = '" + username + "';";
        try {
            // Execute the update
            int rowsAffected = stmt.executeUpdate(sqlUpdate);
            if (rowsAffected > 0) {
                // Update the username in the object only if the database update was successful
                this.username = newUsername;
                System.out.println("Username updated successfully");
            } else {
                System.out.println("No user found with the current username");
            }
        } catch (Exception e) {
            System.out.println("Error updating username: " + e.getMessage());
        }
    }
}