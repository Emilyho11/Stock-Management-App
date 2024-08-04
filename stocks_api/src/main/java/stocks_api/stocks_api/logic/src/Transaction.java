package stocks_api.stocks_api.logic.src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

public class Transaction{

    private int transactionId;
    private String type;
    private Date date;
    private String symbol;
    private double price;
    private int quantity;

    public Transaction() {
        //String username, String target, String status, Date rejection
        // this.username = username;
        // this.targetFriendUsername = target;
        // this.status = status;
        // this.timeRejected = rejection;
    }

    //creates a new id for a transaction
    private static int getNewId(int portfolioId, Connection conn){
        
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT MAX(transaction_id) FROM transaction WHERE portfolio_id = ?;");
            stmt.setInt(1, portfolioId);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Generated a new transaction id successfully");
            if (rs.next()){
                return (rs.getInt(1) + 1);
            }
            return 0;
        } catch (SQLException ex) {
            System.out.println("Error getting a new transaction id" + ex.getMessage());
            return -1;
        }
    }

    //returns all transactions for a given portfolio
    public static ResultSet getTransactions(int portfolioId, Connection conn){
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT transaction_id, transaction_type, transaction_date, symbol, price, quantity FROM transaction WHERE portfolioId = ?;");
            stmt.setInt(1, portfolioId);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Retrieved transactions successfully");
            return rs;
        } catch (SQLException ex) {
            System.out.println("Error getting transactions" + ex.getMessage());
            return null;
        }
    }

    //creates a new transaction of selling a stock
    public static double sellStockTransaction(int portfolioId, String symbol, int quantity, Connection conn){
        try {
            int tid = getNewId(portfolioId, conn);
            double stockprice = Stocks.getCurrentStockPrice(symbol);
            PreparedStatement stmt;
            stmt = conn.prepareStatement("INSERT INTO transaction (transaction_id, portfolio_id, transaction_type, transaction_date, symbol, price, quantity) VALUES (?, ?, 'sell', ?, ?, ?, ?);");
            stmt.setInt(1, tid);
            stmt.setInt(2, portfolioId);
            java.sql.Date today = new java.sql.Date(Calendar.getInstance().getTime().getTime());
            stmt.setDate(3, today);
            stmt.setString(4, symbol);
            stmt.setDouble(5, stockprice);
            stmt.setInt(6, quantity);
            stmt.executeUpdate();
            System.out.println("Created sell stock transaction successfully");
            return stockprice*quantity;
        } catch (SQLException ex) {
            System.out.println("Error creating sell stock transaction" + ex.getMessage());
            return -1;
        }
    }

    //creates a new transaction of buying a stock
    public static double buyStockTransaction(int portfolioId, String symbol, int quantity, Connection conn){
        try {
            int tid = getNewId(portfolioId, conn);
            double stockprice = Stocks.getCurrentStockPrice(symbol);
            PreparedStatement stmt;
            stmt = conn.prepareStatement("INSERT INTO transaction (transaction_id, portfolio_id, transaction_type, transaction_date, symbol, price, quantity) VALUES (?, ?, 'buy', ?, ?, ?, ?);");
            stmt.setInt(1, tid);
            stmt.setInt(2, portfolioId);
            java.sql.Date today = new java.sql.Date(Calendar.getInstance().getTime().getTime());
            stmt.setDate(3, today);
            stmt.setString(4, symbol);
            stmt.setDouble(5, stockprice);
            stmt.setInt(6, quantity);
            stmt.executeUpdate();
            System.out.println("Created buy stock transaction successfully");
            return stockprice*quantity;
        } catch (SQLException ex) {
            System.out.println("Error creating buy stock transaction" + ex.getMessage());
            return -1;
        }
    }
}
