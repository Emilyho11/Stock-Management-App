package cs.toronto.edu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import backend_old.src.User;

// import io.github.cdimascio.dotenv.Dotenv;

public class Main {
	public static void main(String[] args) {
		Connection conn = null;
		Statement stmt = null;

		try {
			// 
			// Register the PostgreSQL driver
			//
			//
			Class.forName("org.postgresql.Driver");

			//
			// Connect to the database
			//
			//
			conn = DriverManager.getConnection("jdbc:postgresql://34.130.14.35:5432/mydb", "postgres", "postgres");
			System.out.println("Opened database successfully"); 

			//
			// Create a statement object
			//
			//
			stmt = conn.createStatement();

			//
			// Create SQL statement to insert a tuple
			//
			//
			// String sqlInsert = "INSERT INTO testtbl (name, value) " +
			// 	"VALUES ('world', 1024);";
			// stmt.executeUpdate(sqlInsert);
			// System.out.println("Tuple inserted successfully");

			// Verify the columns in the User table
            // String verifyColumns = "SELECT column_name FROM information_schema.columns WHERE table_name = 'Friendship';";
            // ResultSet rsColumns = stmt.executeQuery(verifyColumns);
            // System.out.println("Columns in User table:");
            // while (rsColumns.next()) {
            //     String columnName = rsColumns.getString("column_name");
            //     System.out.println(columnName);
            // }
            // rsColumns.close();

			//
			// Create SQL statement to query all tuples
			//
			//
			String getAllUsers = "SELECT username, email FROM user;";
			ResultSet rs = stmt.executeQuery(getAllUsers);

			//
			// Print the queried tuples
			//
			//
			System.out.println("Table user contains the following tuples:\nusername \temail");
			while (rs.next()) {
				String username = rs.getString("username");
				String email = rs.getString("email");
				System.out.println(username + ", " + email);
			}

			// Create a new user
			User user = new User("john_doe", "password123", "john.doe@email.com");
			System.out.println("Username: " + user.getUsername());
			System.out.println("Email: " + user.getEmail());
			System.out.println("Password matches: " + user.checkPassword("password123"));
			// Insert the user to the database
			// user.insertUser(stmt);

			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(1);
		} finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			
				System.out.println("Disconnected from the database");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
