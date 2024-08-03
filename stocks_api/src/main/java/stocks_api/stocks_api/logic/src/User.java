package stocks_api.stocks_api.logic.src;

import java.sql.ResultSet;
import java.sql.Statement;

public class User extends Table {
    public static final String TABLE_NAME = "users";
    public String f_username;
    public String f_password; // Stored as a hash
    public String f_email;

    public User() {};
    
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getKey() {
        return f_username;
    }

    @Override
    public void setKey(String key) {
        if (key == null) {
            this.f_username = null;
            return;
        }
        this.f_username = key;
    }

    @Override
    protected String getWhereIdentifier() {
        return "username = '" + f_username + "'";
    }

    @Override
    public String toString() {
        return "{username: " + this.f_username + ", email: " + this.f_email + "}";
    }

    public String getUsername() {
        return f_username;
    }

    public String getPassword() {
        return f_password;
    }

    public String getEmail() {
        return f_email;
    }

    public void setUsername(String f_username) {
        this.f_username = f_username;
    }

    public void setPassword(String f_password) {
        this.f_password = f_password;
    }

    public void setEmail(String f_email) {
        this.f_email = f_email;
    }

    public boolean checkPassword(String f_password) {
        return this.f_password.equals(f_password);
    }

    // Find by key
    public static User findByUsername(String f_username) {
        try {
            ResultSet rs = Table.db.executeQuery("SELECT * FROM " + TABLE_NAME + " WHERE username = '" + f_username + "';");
            if (rs.next()) {
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                return user;
            }
        } catch (Exception e) {
            System.out.println("Error finding user: " + e.getMessage());
        }
        return null;
    }

    // Check if user exists in database and return true if it exists and false otherwise
    public static boolean userExists(String f_username) {
        try {
            ResultSet rs = Table.db.executeQuery("SELECT username FROM " + TABLE_NAME + " WHERE username = '" + f_username + "';");
            return rs.next() ? true : false;
        } catch (Exception e) {
            System.out.println("Error finding user: " + e.getMessage());
        }
        return false;
    }
}