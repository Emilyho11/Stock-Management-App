
package backend_old.src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import backend_old.src.CashHistory;


public class Portfolio {

    private String owner;
    private String name;
    private double beta;
    private int id;
    private double cash;

    public Portfolio() {
        // this.name = name;
        // this.owner = owner;
        // this.beta = 0;
        // this.id = 0;
        // this.cash = 0;
    }

    public String getOwner() {
        return this.owner;
    }

    public String getName() {
        return this.name;
    }

    public double getBeta() {
        return this.beta;
    }

    public int getId() {
        return this.id;
    }

    private static int getNewId(Connection conn){
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT MAX(portfolioId) FROM portfolio;");
            ResultSet rs = stmt.executeQuery();
            System.out.println("Generated a new portfolio id successfully");
            return (rs.getInt("id") + 1);
        } catch (SQLException ex) {
            System.out.println("Error getting a new portfolio id" + ex.getMessage());
            return -1;
        }
    }

    //adds portfolio to database and the owner/id tuple into the owns table
    public static void createNewUserPortfolio(String owner, String name, Connection conn){
        int newid = getNewId(conn);
        if(newid == -1){
            try {
                createNewPortfolio(name, newid, conn);
                PreparedStatement stmt;
                stmt = conn.prepareStatement("INSERT INTO owns (username, portfolioId) VALUES (?, ?);");
                stmt.setString(1, owner);
                stmt.setInt(2, newid);
                stmt.executeUpdate();
                System.out.println("Created a new portfolio successfully for this user");
            } catch (Exception ex) {
                System.out.println("Error creating a new portfolio for this user" + ex.getMessage());
            }
        }
        else {
            System.out.println("Error getting a new portfolio id");
        }
    }

    private static void createNewPortfolio(String name, int id, Connection conn){
        //adds portfolio to database
        try {
                PreparedStatement stmt;
                stmt = conn.prepareStatement("INSERT INTO portfolio (portfolioId, name, beta) VALUES (?, ?, 0);");
                stmt.setInt(1, id);
                stmt.setString(2, name);
                stmt.executeUpdate();
                System.out.println("Created a new portfolio successfully");
            } catch (Exception ex) {
                System.out.println("Error creating a new portfolio" + ex.getMessage());
            }
        }
    }

    // Find the portfolio in the database by the user
    public static ResultSet findByOwner(String username, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT * FROM portfolio "
                    + "WHERE (username = ?);");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Retrieved this user's portfolio successfully");
            return rs;
        } catch (SQLException ex) {
            System.out.println("Error finding this user's portfolio: " + ex.getMessage());
            return null;
        }
    }

    public static ResultSet findByOwnerAndName(String username, String name, Connection conn) {
        // Find the portfolio in the database by the name and user
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT * FROM portfolio "
                    + "WHERE (username = ? && name = ?);");
            stmt.setString(1, username);
            stmt.setString(2, name);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Retrieved this user's portfolio with that name successfully");
            return rs;
        } catch (SQLException ex) {
            System.out.println("Error finding this user's portfolio: " + ex.getMessage());
        }
        return null;    
    }

    //check the portfolio db to see if this portfolio exists or not
    private static boolean checkPortfolioExist(int id, Connection conn){
        // Find the portfolio in the database by the id
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT EXISTS(SELECT * FROM portfolio WHERE portfolioId = ?);");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Checked for this portfolio id successfully");
            return rs.isBeforeFirst();
        } catch (SQLException ex) {
            System.out.println("Error finding this user's portfolio: " + ex.getMessage());
        }
        return false;
    }

    // public static StockList* findStockLists() {
    //     // Find the portfolio in the database by the name and user
    //     return null;
    // }

    // public static Stock* findStocks(User owner, String name) {
    //     // Find the portfolio in the database by the name and user
    //     return null;
    // }

    // public static Transactions* getTransactions(){

    // }

    //gets all the cash transactions for a given portfolio
    public static ResultSet getPortfolioCashHistory(int portfolioId, Connection conn){
        if (checkPortfolioExist(portfolioId, conn)){
            ResultSet rs = CashHistory.getCashHistory(portfolioId, conn);
            System.out.println("Retrieved this portfolios cash history successfully");
            return rs;
        }
        System.out.println("Error there is no portfolio with this id");
    }

    public static void withdrawCash(int portfolioId, double amount, Connection conn){
        if (checkPortfolioExist(portfolioId, conn)){
            CashHistory.performCashTransaction(portfolioId, "withdraw", amount, conn);
            System.out.println("Withdrew cash from this portfolio successfully");
        }
        System.out.println("Error there is no portfolio with this id");
    }

    public static void depositCash(int portfolioId, double amount, Connection conn){
        if (checkPortfolioExist(portfolioId, conn)){
            CashHistory.performCashTransaction(portfolioId, "deposit", amount, conn);
            System.out.println("Deposited cash from this portfolio successfully");
        }
        System.out.println("Error there is no portfolio with this id");
    }

    public void deletePortfolio() {
        // Delete the portfolio from the database
    }
}
