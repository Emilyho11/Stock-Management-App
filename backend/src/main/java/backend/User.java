package backend_old.src;
import java.sql.ResultSet;
import java.sql.Statement;

public class User extends Table {
    public static String TABLE_NAME = "users";
    private String username;
    private String password; // Stored as a hash
    private String email;

    public User() {};

    // private User(String username, String password, String email) {
    //     this.username = username;
    //     this.password = password;
    //     this.email = email;
    // }

    // // Creates an entry in the database and returns the object
    // public static User create(String username, String password, String email) {
    //     try {
    //         String values = String.format("('%s', '%s', '%s')", username, password, email);
    //         int change = Table.db.executeUpdate("INSERT INTO " + TABLE_NAME + " (username, password, email) VALUES " + values + ";");

    //         // Ensure the user was created
    //         if (change > 0) {
    //             return new User(username, password, email);
    //         }

    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         System.out.println("Error creating user: " + e.getMessage());
    //     }

    //     return null;
    // }
    

    @Override
    public String getKey() {
        return username;
    }

    @Override
    public void setKey(String key) {
        if (key == null) {
            this.username = null;
            return;
        }
        this.username = key;
    }

    @Override
    protected String getWhereIdentifier() {
        return "'username = '" + username + "'";
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

    // For each other class like friends and reviews, we have a static function that will take only a key. It will call the table function (internal version) and it will pass the table name (decorator).


    // public static User findByUsername(Statement stmt, ResultSet rs, String username) {
    //     // Find the user in the database by username
    //     String sqlFind = "SELECT * FROM users WHERE username = '" + username + "';";
    //     try {
    //         rs = stmt.executeQuery(sqlFind);
    //         if (rs.next()) {
    //             username = rs.getString("username");
    //             String email = rs.getString("email");
    //             String password = rs.getString("password");
    //             return new User(username, password, email);
    //         }
    //     } catch (Exception e) {
    //         System.out.println("Error finding user: " + e.getMessage());
    //     }
    //     return null;
    // }

    // public void insertUser(Statement stmt) {
    //     // Insert the user to the database if the username is unique
    //     if (findByUsername(stmt, null, this.username) != null) {
    //         System.out.println("A user with this username already exists");
    //         return;
    //     }
    //     String sqlInsert = "INSERT INTO users (username, password, email) " +
    //         "VALUES ('" + this.username + "', '" + this.password + "', '" + this.email + "');";
        
    //         try {
    //             stmt.executeUpdate(sqlInsert);
    //             System.out.println("User inserted successfully");
    //     } catch (Exception e) {
    //         System.out.println("Error inserting user: " + e.getMessage());
    //     }
    // }

    // public static void printAllUsers(Statement stmt, ResultSet rs) {
    //     // Print all users in the database
    //     String getAllUsers = "SELECT username, email FROM users;";
    //     try {
    //         rs = stmt.executeQuery(getAllUsers);
    //         System.out.println("Table User contains the following tuples:\nusername \temail");
    //         while (rs.next()) {
    //             String username = rs.getString("username");
    //             String email = rs.getString("email");
    //             System.out.println(username + ", " + email);
    //         }
    //     } catch (Exception e) {
    //         System.out.println("Error printing all users: " + e.getMessage());
    //     }
    // }
}