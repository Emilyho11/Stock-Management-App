package cs.toronto.edu;
import java.sql.ResultSet;
import java.sql.SQLException;

import cs.toronto.edu.src.DBHandler;
import cs.toronto.edu.src.Reviews;
import cs.toronto.edu.src.Stocks;
import cs.toronto.edu.src.Table;
import cs.toronto.edu.src.TableManager;
import cs.toronto.edu.src.User;

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
			// Table.clearTable(Stocks.TABLE_NAME);
			// Table.clearTable(User.TABLE_NAME);
			// Table.clearTable(Reviews.TABLE_NAME);

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
			// stockTest.delete();

			// Test reviews
			// Reviews reviewTest = Reviews.create(Reviews::new, "john_doe", 2, "Review 2");
			// Reviews reviewTest2 = Reviews.create(Reviews::new, "john_doe", 3, "Review 1");
			// Reviews reviewTest3 = Reviews.create(Reviews::new, "emilyho", 1, "Emily's review");
			
			// reviewTest.delete();
			
			// Call function: public static <T extends Table<T>> ArrayList<T> printAll(Supplier<? extends T> constructor) and print the entries
			Table.printAll(Reviews.TABLE_NAME);
			// get all rows returns all rows

			// Print rows by column
			// Table.printRowsByColumn(Reviews.TABLE_NAME, "username", "john_doe");

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
