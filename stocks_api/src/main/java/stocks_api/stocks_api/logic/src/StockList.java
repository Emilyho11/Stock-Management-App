package stocks_api.stocks_api.logic.src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StockList extends Table {
    private static final String TABLE_NAME = "stock_list";
    public int id;
    private String name;
    private String privacy;

    public StockList(int id, String name, String privacy) {
        this.id = id;
        this.name = name;
        this.privacy = privacy;
    }
    
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String getWhereIdentifier() {
        return "stock_list_id = " + id;
    }

    @Override
    public String getKey() {
        return Integer.toString(this.id);
    }

    @Override
    public void setKey(String key) {
        if (key == null) {
            this.id = 0;
            return;
        }
        this.id = Integer.parseInt(key);
    }

    public String getName() {
        return name;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setStockListId(int stockListId) {
        this.id = stockListId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    //creates a new stock list id
    private static int getNewId(Connection conn){
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT MAX(stocklist_id) FROM stock_list;");
            ResultSet rs = stmt.executeQuery();
            System.out.println("Generated a new stock list id successfully");
            if (rs.next()){
                return (rs.getInt(1) + 1);
            }
            return 0;
        } catch (SQLException ex) {
            System.out.println("Error getting a new stock list id");
            ex.printStackTrace();
            return -1;
        }
    }

    //adds a new stock list to the database
    public static int createStockList(String owner, String name, String privacy, Connection conn){
        int newid = StockList.getNewId(conn);
        if(newid > -1){
            try {
                PreparedStatement stmt1;
                stmt1 = conn.prepareStatement("INSERT INTO stock_list (stocklist_id, name, privacy) VALUES (?, ?, ?);");
                stmt1.setInt(1, newid);
                stmt1.setString(2, name);
                stmt1.setString(3, privacy);
                stmt1.executeUpdate();
                System.out.println(newid);
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

     // Returns all public stock lists, not including users
     public static ResultSet getOtherPublicStockLists(String username, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT c.stocklist_id, s.name, c.username" +
                                " FROM created c" + 
                                " INNER JOIN stock_list s ON c.stocklist_id = s.stocklist_id" +
                                " WHERE s.privacy = 'public' AND c.username != ?;");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs;
        } catch (Exception ex) {
            System.out.println("Error getting other public stock lists");
            ex.printStackTrace();
            return null;
        }
    }

    // Gets the current total of a stock in a list
    public static int getTotalStock(int id, String symbol, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT quantity FROM lists "
                    + "WHERE (symbol = ? AND stocklist_id = ?);");
            stmt.setString(1, symbol);
            stmt.setInt(2, id);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Retrieved this user's stocks in the stock list successfully");
            if (rs.next()){
                return rs.getInt(1);
            }
            return -1;
        } catch (SQLException ex) {
            System.out.println("Error finding this user's stock lists stocks: ");
            ex.printStackTrace();
            return -1;
        }
    }

    // Gets the all the stocks in a list
    public static ResultSet getStocks(int id, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT symbol, quantity FROM lists "
                    + "WHERE (stocklist_id = ?);");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Retrieved this user's stock list stocks");
            return rs;
        } catch (SQLException ex) {
            System.out.println("Error finding this user's stock list stocks: ");
            ex.printStackTrace();
            return null;
        }
    }

    //creates a new lists tuple
    private static void createListStock(int stocklist_id, String symbol, int total, Connection conn){
        try {
                PreparedStatement stmt;
                stmt = conn.prepareStatement("INSERT INTO lists (stocklist_id, symbol, quantity) VALUES (?, ?, ?);");
                stmt.setInt(1, stocklist_id);
                stmt.setString(2, symbol);
                stmt.setInt(3, total);
                stmt.executeUpdate();
            System.out.println("Created lists tuple successfully");
        } catch (SQLException ex) {
            System.out.println("Error creating lists tuple portfolio");
            ex.printStackTrace();
        }
    }

    //updates a lists tuple
    private static void updateListStock(int stocklist_id, String symbol, int newtotal, Connection conn){
        try {
                PreparedStatement stmt;
                stmt = conn.prepareStatement("UPDATE lists SET quantity = ? WHERE (stocklist_id = ? AND symbol = ?);");
                stmt.setInt(1, newtotal);
                stmt.setInt(2, stocklist_id);
                stmt.setString(3, symbol);
                stmt.executeUpdate();
                System.out.println("Updated lists tuple successfully");
        } catch (SQLException ex) {
            System.out.println("Error updating lists tuple");
            ex.printStackTrace();
        }
    }

    //updates the stock on the lists table to factoring in the new quantity
    public static void listStocksUpdate(int stocklist_id, String symbol, String type, int quantity, Connection conn){
        int totalStock = getTotalStock(stocklist_id, symbol, conn);
        if (type.equals("add") && totalStock > -1){    
            updateListStock(stocklist_id, symbol, totalStock+quantity, conn);
        }
        else if (type.equals("remove") && totalStock > -1){
            updateListStock(stocklist_id, symbol, totalStock-quantity, conn);
        }
    }

    //creates a lists tuple for a adding a new stock to it
    public static void listStocksCreate(int stocklist_id, String symbol, String type, int quantity, Connection conn){
        int totalStock = getTotalStock(stocklist_id, symbol, conn);
        if (type.equals("add") && totalStock == -1){    
            createListStock(stocklist_id, symbol, quantity, conn);
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
            System.out.println("Checking if id " + stocklist_id + " is private");
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
