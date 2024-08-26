package stocks_api.stocks_api.logic.src;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

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
    // Calculates the COV based on stock_data table and either updates
    // the COV if the stock already exists in the table or inserts a new stock
    public static void insertAndUpdateStock(String symbol) {
        try {
            String sqlQuery = "INSERT INTO stocks (symbol, cov) " +
                              "VALUES (?, (SELECT STDDEV_POP(CAST(close AS numeric)) / AVG(CAST(close AS numeric))" +
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

    // Gets the COV for a specific stock sample based on bought and stock_data tables
    public static Map<String, Double> calculatePortfolioCOV(int portfolioId) {
        Map<String, Double> covMap = new HashMap<>();
        try {
            String sqlQuery = "SELECT " +
                              "stock_data.symbol, " +
                              "(STDDEV_SAMP(CAST(stock_data.close AS numeric)) / AVG(CAST(stock_data.close AS numeric))) AS cov " +
                              "FROM bought " +
                              "INNER JOIN stock_data ON bought.symbol = stock_data.symbol " +
                              "WHERE portfolio_id = ? " +
                              "GROUP BY stock_data.symbol;";
            PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlQuery);
            preparedStatement.setInt(1, portfolioId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String symbol = rs.getString("symbol");
                Double COV = rs.getDouble("cov");
                covMap.put(symbol, COV);
            }
            return covMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return covMap;
    }

    // Calculate correlation function:
    // Calculating the population covariance between stock_data1.close and stock_data2.close.
    // Dividing the covariance by the product of the population standard deviations of stock_data1.close and stock_data2.close.
    // Using an INNER JOIN on the timestamp to align the closing prices of the two stocks.
    public static double calculateCorrelation(String symbol1, String symbol2) {
        try {
            String sqlQuery = "SELECT " +
                            "(COVAR_POP(CAST(stock_data1.close AS numeric), CAST(stock_data2.close AS numeric)) / " +
                            "(STDDEV_POP(CAST(stock_data1.close AS numeric)) * STDDEV_POP(CAST(stock_data2.close AS numeric)))) AS correlation " +
                            "FROM stock_data stock_data1 " +
                            "INNER JOIN stock_data stock_data2 " +
                            "ON stock_data1.timestamp = stock_data2.timestamp " +
                            "WHERE stock_data1.symbol = ? " +
                            "AND stock_data2.symbol = ?;";
            PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlQuery);
            preparedStatement.setString(1, symbol1);
            preparedStatement.setString(2, symbol2);
            ResultSet rs = preparedStatement.executeQuery();
            double correlation = 0;
            if (rs.next()) {
                correlation = rs.getDouble("correlation");
                System.out.println("Correlation between " + symbol1 + " and " + symbol2 + ": " + correlation);
            }
            return correlation;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // COV calculation between two symbols
    public static double calculateCOVBetweenTwoStocks(String symbol1, String symbol2) {
        try {
            // Query to calculate the COV for each stock
            String sqlQuery = "WITH stock1_stats AS (" +
                              "SELECT AVG(CAST(close AS numeric)) AS mean1, " +
                              "STDDEV_POP(CAST(close AS numeric)) AS stddev1 " +
                              "FROM stock_data " +
                              "WHERE symbol = ?), " +
                              "stock2_stats AS (" +
                              "SELECT AVG(CAST(close AS numeric)) AS mean2, " +
                              "STDDEV_POP(CAST(close AS numeric)) AS stddev2 " +
                              "FROM stock_data " +
                              "WHERE symbol = ?) " +
                              "SELECT (stddev1 / mean1) AS cov1, " +
                              "(stddev2 / mean2) AS cov2 " +
                              "FROM stock1_stats, stock2_stats;";
            
            PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlQuery);
            preparedStatement.setString(1, symbol1);
            preparedStatement.setString(2, symbol2);
            ResultSet rs = preparedStatement.executeQuery();
            double covariance = 0;
            if (rs.next()) {
                covariance = rs.getDouble("cov1") * rs.getDouble("cov2");
                System.out.println("Covariance between " + symbol1 + " and " + symbol2 + ": " + covariance);
            }
            return covariance;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
