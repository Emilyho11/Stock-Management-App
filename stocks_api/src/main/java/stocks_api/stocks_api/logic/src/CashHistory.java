package stocks_api.stocks_api.logic.src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

public class CashHistory{

    private int portfolio_id;
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

    //searches the db for the most recent balance of a portfolio
    public static double getBalance(int id, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT balance FROM cash_history WHERE (portfolio_id = ?) ORDER BY timestamp DESC FETCH FIRST 1 ROW ONLY;");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Retrieved the latest balance of the portfolio");
            if (rs.next()){
                return rs.getDouble(1);
            }
            return 0;
        } catch (SQLException ex) {
            System.out.println("Error getting a portfolios balance" + ex.getMessage());
            return -1;
        }
    }

    //retrieves from the db all the past cash transactions for a portfolio
    public static ResultSet getCashHistory(int id, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT timestamp, type, amount, balance FROM cash_history WHERE (portfolio_id = ?);");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Retrieved the cash transactions of the portfolio");
            return rs;
        } catch (SQLException ex) {
            System.out.println("Error getting a portfolios balance" + ex.getMessage());
            return null;
        }
    }

    //creates a new cash transaction and withdraws cash from a portfolio
    private static void withdrawCash(int id, double amount, double balance, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("INSERT INTO cash_history (portfolio_id, timestamp, type, amount, balance) "
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
    private static void depositCash(int id, double amount, double balance, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("INSERT INTO cash_history (portfolio_id, timestamp, type, amount, balance) "
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
    public static void performCashTransaction(int id, String type, double amount, Connection conn){
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
