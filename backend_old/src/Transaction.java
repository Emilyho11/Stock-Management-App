package backend_old.src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

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
        return -1;
    }

    public static void sellStock(int portfolioId, String symbol, int quantity){

    }

    public static void buyStock(int portfolioId, String symbol, int quantity){

    }
}
