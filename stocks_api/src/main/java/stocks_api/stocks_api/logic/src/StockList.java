package stocks_api.stocks_api.logic.src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StockList extends Table {
    private static final String TABLE_NAME = "stock_list";
    private int stockListId;
    private String name;
    private Boolean privacy;

    public StockList() {}
    
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String getWhereIdentifier() {
        return "stock_list_id = " + stockListId;
    }

    @Override
    public String getKey() {
        return Integer.toString(this.stockListId);
    }

    @Override
    public void setKey(String key) {
        if (key == null) {
            this.stockListId = 0;
            return;
        }
        this.stockListId = Integer.parseInt(key);
    }

    public String getName() {
        return name;
    }

    public Boolean getPrivacy() {
        return privacy;
    }

    public void setStockListId(int stockListId) {
        this.stockListId = stockListId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrivacy(Boolean privacy) {
        this.privacy = privacy;
    }

    //creates a new stock list id
    private static int getNewId(Connection conn){
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT MAX(stocklist_id) FROM created;");
            ResultSet rs = stmt.executeQuery();
            System.out.println("Generated a new stock list id successfully");
            if (rs.next()){
                return rs.getInt(1) + 1;
            }
            return 0;
        } catch (SQLException ex) {
            System.out.println("Error getting a new stock list id");
            ex.printStackTrace();
            return -1;
        }
    }

    //adds a new stock list to the database
    public static int createStockList(String owner, String name, Connection conn){
        int newid = StockList.getNewId(conn);
        if(newid > -1){
            try {
                PreparedStatement stmt1;
                stmt1 = conn.prepareStatement("INSERT INTO stock_list (stocklist_id, name, privacy) VALUES (?, ?, 'private');");
                stmt1.setInt(1, newid);
                stmt1.setString(2, name);
                stmt1.executeUpdate();
                System.out.println("Created a new stock list successfully");
                return newid;
            } catch (Exception ex) {
                System.out.println("Error creating a new stock list");
                ex.printStackTrace();
                return newid;
            }
        }
        else {
            System.out.println("Error getting a new stock list id");
            return newid;
        }
    }

    //changes the privacy of a given stock list
    public static void changePrivacy(int id, String privacy, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("UPDATE stock_list "
                    + "SET privacy = ? "
                    + "WHERE (stocklist_id = ?);");
            stmt.setString(1, privacy);
            stmt.setInt(2, id);
            stmt.executeUpdate();
            System.out.println("Stocklist privacy changed successfully.");
        } catch (SQLException ex) {
            System.out.println("Error accepting friend request: " + ex.getMessage());
        }
    }

    //changes the name of a stock list
    public static void renameStockList(int stocklist_id, String newname, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("UPDATE stock_list "
                    + "SET name = ? "
                    + "WHERE (stocklist_id = ?);");
            stmt.setString(1, newname);
            stmt.setInt(2, stocklist_id);
            stmt.executeUpdate();
            System.out.println("Renamed stock list successfully");
        } catch (SQLException ex) {
            System.out.println("Error renaming stock list: " + ex.getMessage());
        }
    }

    // Delete the stock list
    private static void deleteStockList(int stocklist_id, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("DELETE FROM stock_list "
                    + "WHERE (stocklist_id = ?);");
            stmt.setInt(1, stocklist_id);
            stmt.executeUpdate();
            System.out.println("Deleted stock list successfully");            
        } catch (SQLException ex) {
            System.out.println("Error deleting stock list: " + ex.getMessage());
        }
    }

    // Delete the stock list completely from the database
    public static void deleteStockListGlobal(int stocklist_id, Connection conn) {
        try {
            StockList.deleteStockList(stocklist_id, conn);
                deleteStockList(stocklist_id, conn);
                PreparedStatement stmt;
                stmt = conn.prepareStatement("DELETE FROM contains "
                        + "WHERE (stocklist_id = ?);");
                stmt.setInt(1, stocklist_id);
                stmt.executeUpdate();
                PreparedStatement stmt2;
                stmt2 = conn.prepareStatement("DELETE FROM created"
                        + "WHERE (stocklist_id = ?);");
                stmt2.setInt(1, stocklist_id);
                stmt2.executeUpdate();
                System.out.println("Deleted stock list globally successfully");
            } catch (SQLException ex) {
                System.out.println("Error deleting stock list globally: " + ex.getMessage());
            }
    }

    // Check if stocklist is private or not
    public static boolean isPrivate(int stocklist_id, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT privacy FROM stock_list"
                    + "WHERE (stocklist_id = ?);");
            stmt.setInt(1, stocklist_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // return privacy value, which is a boolean
                return rs.getString(1).equals("private");
            }
            return false;
        } catch (SQLException ex) {
            System.out.println("Error checking if stocklist is private: " + ex.getMessage());
            return false;
        }
    }
}
