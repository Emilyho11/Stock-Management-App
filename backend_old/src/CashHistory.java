package backend_old.src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

public class CashHistory {

    private int portfolioId;
    private Date timestamp;
    private String type;
    private double balance;
    private double amount;

    public CashHistory() {
        //String username, String target, String status, Date rejection
        // this.username = username;
        // this.targetFriendUsername = target;
        // this.status = status;
        // this.timeRejected = rejection;
    }

    public double getBalance() {
        return this.balance;
    }

    //searches the db for the most recent balance of a portfolio
    public double getBalance(int id, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT MAX(balance) FROM changes WHERE (portfolioId = ?);");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Retrieved the latest balance of the portfolio");
            return (rs.getDouble("balance"));
        } catch (SQLException ex) {
            System.out.println("Error getting a portfolios balance" + ex.getMessage());
            return -1;
        }
    }

    //creates a new cash transaction and withdraws cash from a portfolio
    private void withdrawCash(int id, double amount, double balance, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("INSERT INTO cash_history (portfolioId, timestamp, type, amount, balance) "
                    + "VALUES (? , ?, 'withdraw', ?, ?);");
            stmt.setInt(1, id);
            java.sql.Timestamp time = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            stmt.setTimestamp(2, time);
            stmt.setDouble(3, amount);
            stmt.setDouble(4, balance);
            stmt.executeUpdate();
            System.out.println("Withdrew cash successfully");
        } catch (SQLException ex) {
            System.out.println("Error withdrawing cash: " + ex.getMessage());
        }
    }

    //creates a new cash transaction and deposits cash from a portfolio
    private void depositCash(int id, double amount, double balance, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("INSERT INTO cash_history (portfolioId, timestamp, type, amount, balance) "
                    + "VALUES (? , ?, 'deposit', ?, ?);");
            stmt.setInt(1, id);
            java.sql.Timestamp time = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            stmt.setTimestamp(2, time);
            stmt.setDouble(3, amount);
            stmt.setDouble(4, balance);
            stmt.executeUpdate();
            System.out.println("Deposited cash successfully");
        } catch (SQLException ex) {
            System.out.println("Error depositing cash: " + ex.getMessage());
        }
    }

    //creates a new cash transaction and changes cash from a portfolio
    //assumes the id given is a valid id (handled by portfolio class)
    public void performCashTransaction(int id, String type, double amount, Connection conn){
        double newbal = getBalance(id, conn);
        if ("withdraw".equals(type)){
            newbal = newbal - amount;
            if (newbal >= 0){
                withdrawCash(id, amount, newbal, conn);
            }
            else {
                System.out.println("Error invalid amount of cash in account to withdraw.");
            }
            return;
        }
        else if ("deposit".equals(type)){
            newbal = newbal + amount;
            depositCash(id, amount, newbal, conn);
            return;
        }
        System.out.println("Error invalid cash transaction type.");
    }
}
