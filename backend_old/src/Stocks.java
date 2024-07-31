package backend_old.src;
import java.sql.ResultSet;
import java.sql.Statement;

public class Stocks {
    private String symbol;
    private Double COV;

    public Stocks(String symbol, Double COV) {
        this.symbol = symbol;
        this.COV = COV;
    }

    public String getSymbol() {
        return symbol;
    }

    public Double getCOV() {
        return COV;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setCOV(Double COV) {
        this.COV = COV;
    }

    public void insertStocks(Statement stmt) {
        // Insert the user to the database
        String sqlInsert = "INSERT INTO stocks (symbol, COV) " +
            "VALUES ('" + symbol + "', '" + COV + "');";
        
            try {
                stmt.executeUpdate(sqlInsert);
                System.out.println("Stocks inserted successfully");
        } catch (Exception e) {
            System.out.println("Error inserting Stocks");
        }
    }

    public void deleteStocks(Statement stmt) {
        // Insert the user to the database
        String sqlDelete = "DELETE FROM stocks WHERE symbol = '" + symbol + "';";
        
            try {
                stmt.executeUpdate(sqlDelete);
                System.out.println("Stocks deleted successfully");
        } catch (Exception e) {
            System.out.println("Error deleting Stocks");
        }
    }

    public static Stocks findBySymbol(Statement stmt, ResultSet rs, String symbol) {
        Stocks stocks = null;
        try {
            String sqlQuery = "SELECT * FROM stocks WHERE symbol = '" + symbol + "';";
            rs = stmt.executeQuery(sqlQuery);
            if (rs.next()) {
                stocks = new Stocks(rs.getString("symbol"), rs.getDouble("COV"));
            }
        } catch (Exception e) {
            System.out.println("Error finding Stocks: " + e.getMessage());
        }
        return stocks;
    }

    public static void printAllStocks(Statement stmt, ResultSet rs) {
        try {
            String sqlQuery = "SELECT * FROM stocks;";
            rs = stmt.executeQuery(sqlQuery);
            System.out.println("Table Stocks contains the following tuples:\nsymbol \tCOV");
            while (rs.next()) {
                String symbol = rs.getString("symbol");
                Double COV = rs.getDouble("COV");
                System.out.println(symbol + ", " + COV);
            }
        } catch (Exception e) {
            System.out.println("Error printing Stocks: " + e.getMessage());
        }
    }

}
