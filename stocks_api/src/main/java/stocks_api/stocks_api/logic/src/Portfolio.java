package stocks_api.stocks_api.logic.src;

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

    public Portfolio() {
        //String username, String target, String status, Date rejection
        // this.username = username;
        // this.targetFriendUsername = target;
        // this.status = status;
        // this.timeRejected = rejection;
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

    //creates a new portfolio id
    private static int getNewId(Connection conn){
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT MAX(portfolio_id) FROM owns;");
            ResultSet rs = stmt.executeQuery();
            System.out.println("Generated a new portfolio id successfully");
            if (rs.next()){
                return rs.getInt(1) + 1;
            }
            return 0;
        } catch (SQLException ex) {
            System.out.println("Error getting a new portfolio id");
            ex.printStackTrace();
            return -1;
        }
    }

    //adds portfolio to database and the owner/id tuple into the owns table
    public static void createPortfolio(String owner, String name, Connection conn){
        int newid = Portfolio.getNewId(conn);
        if(newid > -1){
            try {
                PreparedStatement stmt1;
                stmt1 = conn.prepareStatement("INSERT INTO portfolio (portfolio_id, name, beta) VALUES (?, ?, ?);");
                stmt1.setInt(1, newid);
                stmt1.setString(2, name);
                stmt1.setDouble(3, 0);
                stmt1.executeUpdate();
                System.out.println("Created a new portfolio successfully");
                PreparedStatement stmt;
                stmt = conn.prepareStatement("INSERT INTO owns (username, portfolio_id) VALUES (?, ?);");
                stmt.setString(1, owner);
                stmt.setInt(2, newid);
                stmt.executeUpdate();
                System.out.println("Created a new portfolio successfully for this user");
            } catch (Exception ex) {
                System.out.println("Error creating a new portfolio for this user");
                ex.printStackTrace();
            }
        }
        else {
            System.out.println("Error getting a new portfolio id");
        }
    }

    //changes the name of a portfolio
    public static void renamePortfolio(int portfolio_id, String newname, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("UPDATE portfolio "
                    + "SET name = ? "
                    + "WHERE (portfolio_id = ?);");
            stmt.setString(1, newname);
            stmt.setInt(2, portfolio_id);
            stmt.executeUpdate();
            System.out.println("Renamed portfolio successfully");
        } catch (SQLException ex) {
            System.out.println("Error renaming portfolio: " + ex.getMessage());
        }
    }

    // Find the portfolio in the database by the user
    public static ResultSet findByOwner(String username, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT portfolio_id FROM owns WHERE (username = ?);");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Retrieved this user's portfolio successfully");
            return rs;
        } catch (SQLException ex) {
            System.out.println("Error finding this user's portfolio: ");
            ex.printStackTrace();
            return null;
        }
    }

    // Find the portfolio in the database by the name and user
    public static ResultSet findByOwnerAndName(String username, String name, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT portfolio_id, beta FROM portfolio "
                    + "WHERE ((name = ?) AND portfolio_id IN (SELECT portfolio_id FROM owns WHERE username = ?));");
            stmt.setString(1, name);
            stmt.setString(2, username);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Retrieved this user's portfolio with that name successfully");
            return rs;
        } catch (SQLException ex) {
            System.out.println("Error finding this user's portfolio: ");
            ex.printStackTrace();
            return null;
        }
    }

    // Delete the portfolio from the database
    public static void deletePortfolio(int portfolio_id, Connection conn) {
        try {
            //delete from portfolio db
            PreparedStatement stmt;
            stmt = conn.prepareStatement("DELETE FROM portfolio "
                    + "WHERE (portfolio_id = ?);");
            stmt.setInt(1, portfolio_id);
            stmt.executeUpdate();
            PreparedStatement stmt2;
            stmt2 = conn.prepareStatement("DELETE FROM owns "
                    + "WHERE (portfolio_id = ?);");
            stmt2.setInt(1, portfolio_id);
            stmt2.executeUpdate();
            System.out.println("Deleted portfolio successfully");            
        } catch (SQLException ex) {
            System.out.println("Error deleting portfolio: " + ex.getMessage());
        }
    }
}
