package stocks_api.stocks_api.logic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import stocks_api.stocks_api.logic.src.*;
// import stocks_api.stocks_api.logic.src.DBHandler;
// import stocks_api.stocks_api.logic.src.Reviews;
// import stocks_api.stocks_api.logic.src.Stocks;
// import stocks_api.stocks_api.logic.src.Table;
// import stocks_api.stocks_api.logic.src.TableManager;
// import stocks_api.stocks_api.logic.src.User;

// import io.github.cdimascio.dotenv.Dotenv;

public class Main {
	public static void Initialize() {
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

			// add reviews to reviews table
			Reviews reviewTest = Reviews.create(Reviews::new, "john_doe", 2, "Review 2");
			Reviews reviewTest2 = Reviews.create(Reviews::new, "john_doe", 3, "Review 1");
			Reviews reviewTest3 = Reviews.create(Reviews::new, "emilyho", 1, "Emily's review");
			
			// test parserUtil.java
			ResultSet rs = testingDB.executeQuery("SELECT * FROM reviews;");
			// ParserUtil.resultSetToJson(rs);
			// print out result of parserUtil
			System.out.println(ParserUtil.resultSetToJson(rs));
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(1);
		}


	}

	public void exit() {
		try {
			DBHandler.Close();
		
			System.out.println("Disconnected from the database");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
