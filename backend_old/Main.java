package cs.toronto.edu;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import cs.toronto.edu.src.*;

// import io.github.cdimascio.dotenv.Dotenv;

public class Main {
	public static void main(String[] args) {
		// Connection conn = null;
		// Statement stmt = null;

		try {
			DBHandler.Initialize();
			TableManager tableManager = TableManager.getInstance();
			DBHandler testingDB = DBHandler.getInstance();
			tableManager.addTable(User.TABLE_NAME, User::new);
			tableManager.addTable(Stocks.TABLE_NAME, Stocks::new);
			tableManager.addTable(Reviews.TABLE_NAME, Reviews::new);

			// DEBUGGING
			Table.clearTable(Stocks.TABLE_NAME);
			Table.clearTable(User.TABLE_NAME);
			Table.clearTable(Reviews.TABLE_NAME);

			ResultSet rs = testingDB.executeQuery("SELECT username, email FROM users;");
	
			// Print the queried tuples			
			System.out.println("Table user contains the following tuples:\nusername \temail");
			
			while (rs.next()) {
				String username = rs.getString("username");
				String email = rs.getString("email");
				System.out.println(username + ", " + email);
			}
			
			Stocks stockTest = Stocks.create(Stocks::new, "AAPL", 0.5);

			stockTest.update();
			stockTest.delete();

			// Test reviews
			Reviews reviewTest = Reviews.create(Reviews::new, "john_doe", 1, "This is third review omg");
			
			reviewTest.delete();

			rs.close();


		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(1);
		} finally {
			try {
				DBHandler.Close();
			
				System.out.println("Disconnected from the database");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
