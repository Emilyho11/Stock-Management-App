package backend_old.src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import cs.toronto.edu.src.StockData;

public class Transaction {

    private int transactionId;
    private String type;
    private Date date;
    private String symbol;
    private double price;
    private int quantity;

    //creates a transaction
    public Transaction(String type, String symbol, int quantity) {
    }

    //creates a new id for a transaction
    private static int getNewId(int portfolioId, Connection conn){
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT MAX(transactionId) FROM transaction WHERE portfolioId = ?;");
            stmt.setInt(1, portfolioId);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Generated a new transaction id successfully");
            return (rs.getInt("id") + 1);
        } catch (SQLException ex) {
            System.out.println("Error getting a new transaction id" + ex.getMessage());
            return -1;
        }
    }

    //gets the latest stock price of a given stock
    private static double getCurrentStockValue(String symbol, Connection conn){
        ResultSet rs = StockData.getLatestStockData(symbol, conn);
        try {
            return rs.getDouble("close");
        } catch (SQLException ex) {
            return -1;
        }
    }

    //creates a new transaction of selling a stock
    public static void sellStock(int portfolioId, String symbol, int quantity, Connection conn){
        try {
            int tid = getNewId(portfolioId, conn);
            double stockprice = getCurrentStockValue(symbol, conn);
            PreparedStatement stmt;
            stmt = conn.prepareStatement("INSERT INTO transaction (transactionId, portfolioId, transactionType, transactionDate, symbol, price, quantity) VALUES (?, ?, 'sell', ?, ?, ?, ?);");
            stmt.setInt(1, tid);
            stmt.setInt(2, portfolioId);
            Date today = new Date();
            stmt.setDate(3, (java.sql.Date) today);
            stmt.setString(4, symbol);
            stmt.setDouble(5, stockprice);
            stmt.setInt(6, quantity);
            stmt.executeUpdate();
            System.out.println("Created sell stock transaction successfully");
        } catch (SQLException ex) {
            System.out.println("Error creating sell stock transaction" + ex.getMessage());
        }
    }

    public static void buyStock(int portfolioId, String symbol, int quantity, Connection conn){
        try {
            int tid = getNewId(portfolioId, conn);
            double stockprice = getCurrentStockValue(symbol, conn);
            PreparedStatement stmt;
            stmt = conn.prepareStatement("INSERT INTO transaction (transactionId, portfolioId, transactionType, transactionDate, symbol, price, quantity) VALUES (?, ?, 'buy', ?, ?, ?, ?);");
            stmt.setInt(1, tid);
            stmt.setInt(2, portfolioId);
            Date today = new Date();
            stmt.setDate(3, (java.sql.Date) today);
            stmt.setString(4, symbol);
            stmt.setDouble(5, stockprice);
            stmt.setInt(6, quantity);
            stmt.executeUpdate();
            System.out.println("Created buy stock transaction successfully");
        } catch (SQLException ex) {
            System.out.println("Error creating buy stock transaction" + ex.getMessage());
        }
    }
}
