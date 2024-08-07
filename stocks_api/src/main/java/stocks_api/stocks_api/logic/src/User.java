package stocks_api.stocks_api.logic.src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

    public boolean checkPassword(String username, String password) {
        // Check if password is in database when user = username
        try {
            ResultSet rs = Table.db.executeQuery("SELECT password FROM " + TABLE_NAME + " WHERE username = '" + username + "';");
            if (rs.next()) {
                String dbPassword = rs.getString("password");
                if (dbPassword != null && password != null) {
                    return dbPassword.trim().equals(password.trim());
                }
            }
        } catch (Exception e) {
            System.out.println("Error finding user: " + e.getMessage());
        }
        return false;
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
    public static boolean userExists(String username) {
        try {
            ResultSet rs = Table.db.executeQuery("SELECT username FROM " + TABLE_NAME + " WHERE username = '" + username + "';");
            // if the result set is not empty, the user exists
            return rs.next();
        } catch (Exception e) {
            System.out.println("Error finding user: " + e.getMessage());
        }
        return false;
    }

    // Adds a new stock list to the database (not in a portfolio)
    public static void createStockList(String username, String name, String privacy, Connection conn){
        try {
            int newid = StockList.createStockList(username, name, privacy, conn);
            PreparedStatement stmt1;
            stmt1 = conn.prepareStatement("INSERT INTO created (username, stocklist_id) VALUES (?, ?);");
            stmt1.setString(1, username);
            stmt1.setInt(2, newid);
            stmt1.executeUpdate();
            System.out.println("Created a new stock list for this user successfully");
        } catch (Exception ex) {
            System.out.println("Error creating a new stock list for this user");
            ex.printStackTrace();
        }
    }

    // Returns all stock lists for a given user
    public static ResultSet getUserStockLists(String username, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT stocklist_id, name, privacy FROM stock_list WHERE (stocklist_id IN (SELECT stocklist_id FROM created WHERE (username = ?)));");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs;
        } catch (Exception ex) {
            System.out.println("Error getting stock lists for this user");
            ex.printStackTrace();
            return null;
        }
    }

    // Returns all stock lists shared with the user
    public static ResultSet getFriendStockLists(String username, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT s.stocklist_id, s.name, c.username"
            + " FROM stock_list s"
            + " INNER JOIN created c ON s.stocklist_id = c.stocklist_id"
            + " WHERE s.privacy = 'friends'"
            + " AND c.username IN ("
                + " SELECT target"
                + " FROM friendship"
                + " WHERE username = ?"
                + " AND status = 'Accepted');");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs;
        } catch (Exception ex) {
            System.out.println("Error getting friend stock lists for this user");
            ex.printStackTrace();
            return null;
        }
    }
}