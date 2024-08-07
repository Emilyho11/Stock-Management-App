package stocks_api.stocks_api.logic.src;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Stocks extends Table<Stocks> {
    public static final String TABLE_NAME = "stocks";
    public String f_symbol;
    public Double f_COV;

    public Stocks() {}

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }


    @Override
    protected String getWhereIdentifier() {
        return "symbol = '" + f_symbol + "'";
    }

    @Override
    public String getKey() {
        return f_symbol;
    }

    @Override
    public void setKey(String key) {
        if (key == null) {
            this.f_symbol = null;
            return;
        }
        this.f_symbol = key;
    }

    public String getSymbol() {
        return f_symbol;
    }

    public Double getCOV() {
        return f_COV;
    }

    public void setSymbol(String symbol) {
        this.f_symbol = symbol;
    }

    public void setCOV(Double COV) {
        this.f_COV = COV;
    }

    public static void printAllStocks(Statement stmt, ResultSet rs) {
        try {
            String sqlQuery = "SELECT * FROM stocks;";
            rs = stmt.executeQuery(sqlQuery);
            System.out.println("Table Stocks contains the following tuples:\nsymbol \tCOV");
            while (rs.next()) {
                String symbol = rs.getString("symbol");
                Double COV = rs.getDouble("cov");
                System.out.println(symbol + ", " + COV);
            }
        } catch (Exception e) {
            System.out.println("Error printing Stocks: " + e.getMessage());
        }
    }

    public static double getCurrentStockPrice(String symbol) {
        try {
            String sqlQuery = "SELECT close FROM stock_data WHERE (symbol = ?) ORDER BY timestamp DESC FETCH FIRST 1 ROW ONLY;";
            PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlQuery);
            preparedStatement.setString(1, symbol);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()){
                return rs.getDouble(1);
            }
            return -1;            
            
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Check if symbol exists in the stocks table
    public static boolean symbolExists(String symbol) {
        try {
            String sqlQuery = "SELECT symbol FROM stocks WHERE symbol = ?";
            PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlQuery);
            preparedStatement.setString(1, symbol);
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Calculates COV for all stocks from stock_data
    // INSERT INTO stocks (symbol, cov)
    // SELECT
    //     symbol,
    //     STDDEV(CAST(close AS numeric)) / AVG(CAST(close AS numeric)) AS cov
    // FROM
    //     stock_data
    // GROUP BY
    //     symbol;
    public static void insertStock(String symbol) {
        // Check if symbol exists in the stocks table
        if (symbolExists(symbol)) {
            return;
        }
        try {
            String sqlQuery = "INSERT INTO stocks (symbol, cov) SELECT symbol, STDDEV(CAST(close AS numeric)) / AVG(CAST(close AS numeric)) AS cov FROM stock_data WHERE symbol = ?;";
            PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlQuery);
            preparedStatement.setString(1, symbol);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Update COV for a specific stock
    public static void updateCOV(String symbol) {
        if (!symbolExists(symbol)) {
            return;
        }
        try {
            String sqlQuery = "UPDATE stocks SET cov = STDDEV(CAST(close AS numeric)) / AVG(CAST(close AS numeric)) FROM stock_data WHERE symbol = ?;";
            PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlQuery);
            preparedStatement.setString(1, symbol);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Function to insert and udpate stock into stocks table
    // INSERT INTO stocks (symbol, cov)
    // VALUES (
    //     ?,
    //     (SELECT STDDEV_POP(CAST(close AS numeric)) / AVG(CAST(close AS numeric)) * 100
    //      FROM stock_data
    //      WHERE symbol = ?)
    // )
    // ON CONFLICT (symbol)
    // DO UPDATE SET cov = EXCLUDED.cov;
    public static void insertAndUpdateStock(String symbol) {
        try {
            String sqlQuery = "INSERT INTO stocks (symbol, cov) " +
                              "VALUES (?, (SELECT STDDEV_POP(CAST(close AS numeric)) / AVG(CAST(close AS numeric)) * 100 " +
                              "FROM stock_data WHERE symbol = ?)) " +
                              "ON CONFLICT (symbol) DO UPDATE SET cov = EXCLUDED.cov;";
            PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlQuery);
            preparedStatement.setString(1, symbol);
            preparedStatement.setString(2, symbol);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // When stock_data is being created in database, we must take the symbol,
    // and check if it exists already in the stocks table. If it does not exist,
    // we must create a new entry in the stocks table, along with its COV calculation.
    // Method to insert a new stock entry into the database
}
