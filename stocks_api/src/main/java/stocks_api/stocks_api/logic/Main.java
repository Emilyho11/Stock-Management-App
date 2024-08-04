package stocks_api.stocks_api.logic;

import java.sql.SQLException;

import stocks_api.stocks_api.logic.src.DBHandler;
import stocks_api.stocks_api.logic.src.Reviews;
import stocks_api.stocks_api.logic.src.Stocks;
import stocks_api.stocks_api.logic.src.TableManager;
import stocks_api.stocks_api.logic.src.User;
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

			// add reviews to reviews table
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
