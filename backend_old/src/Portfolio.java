
package backend_old.src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Portfolio {
    private String owner;
    private String name;
    private double beta;
    private int id;
    private double cash;

    public Portfolio(String name, String owner) {
        this.name = name;
        this.owner = owner;
        this.beta = 0;
        this.id = 0;
        this.cash = 0;
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

    private int getNewId(Connection conn){
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT MAX(portfolioId) FROM portfolio;");
            ResultSet rs = stmt.executeQuery();
            System.out.println("Generated a new portfolio id successfully");
            return (rs.getInt(id) + 1);
        } catch (SQLException ex) {
            System.out.println("Error getting a new portfolio id" + ex.getMessage());
            return -1;
        }
    }

    private void createNewUserPortfolio(String owner, String name, Connection conn){
        //adds portfolio to database and the owner/id tuple into the owns table
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

    private void createNewPortfolio(String name, int id, Connection conn){
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

    public static ResultSet findByOwner(String username, Connection conn) {
        // Find the portfolio in the database by the user
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
        }
        return null;
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

    // public static CashHistory* getCashHistory(){

    // }

    public static withdraw(double amount){
        this.cash = this.cash - amount;
        //this also needs to record this into the cash history
    }

    public static deposit(double amount){
        this.cash = this.cash + amount;
        //this also needs to record this into cash history
    }

    public void deletePortfolio() {
        // Delete the portfolio from the database
    }
}
