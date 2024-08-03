package stocks_api.stocks_api.logic.src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            stmt = conn.prepareStatement("SELECT MAX(transactionId) FROM transaction WHERE portfolioId = ?;");
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
}
