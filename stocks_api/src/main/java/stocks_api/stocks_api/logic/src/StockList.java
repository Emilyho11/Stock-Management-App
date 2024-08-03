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

    // public static StockList findByStockListId(int stockListId) {
    //     StockList stockList = null;
    //     try {

    //         String sqlQuery = "SELECT * FROM stock_list WHERE stock_list_id = " + stockListId + ";";
    //         // ResultSet rs = stmt.executeQuery(sqlQuery);
    //         ResultSet rs = db.executeQuery(sqlQuery);

    //         if (rs.next()) {
    //             stockList = new StockList(rs.getInt("stock_list_id"), rs.getString("name"), rs.getBoolean("privacy"));
    //         }
    //     } catch (Exception e) {
    //         System.out.println("Error finding StockList: " + e.getMessage());
    //     }
    //     return stockList;
    // }

}
