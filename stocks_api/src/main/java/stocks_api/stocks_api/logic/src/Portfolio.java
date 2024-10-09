package stocks_api.stocks_api.logic.src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Portfolio {

    private String name;
    private double beta;
    private int id;

    public Portfolio(int id, String name, double beta) {
        this.name = name;
        this.beta = beta;
        this.id = id;
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
            stmt = conn.prepareStatement("SELECT portfolio_id, name, beta FROM portfolio WHERE (portfolio_id IN (SELECT portfolio_id FROM owns WHERE (username = ?)));");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
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
            return rs;
        } catch (SQLException ex) {
            System.out.println("Error finding this user's portfolio: ");
            ex.printStackTrace();
            return null;
        }
    }

    //searches the db for the most recent balance of a portfolio
    public static double getBalance(int id, Connection conn) {
        return CashHistory.getBalance(id, conn);
    }

    //gets all the cash transactions for a given portfolio
    public static ResultSet getPortfolioCashHistory(int portfolioId, Connection conn){
        ResultSet rs = CashHistory.getCashHistory(portfolioId, conn);
        return rs;
    }

    public static void withdrawCash(int portfolioId, double amount, Connection conn){
        CashHistory.performCashTransaction(portfolioId, "withdraw", amount, conn);
        System.out.println("Withdrew cash from this portfolio successfully");
    }

    public static void depositCash(int portfolioId, double amount, Connection conn){
        CashHistory.performCashTransaction(portfolioId, "deposit", amount, conn);
        System.out.println("Deposited cash from this portfolio successfully");
    }

    // Gets the current total of a stock in a portfolio
    public static int getTotalStock(int id, String symbol, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT total FROM bought "
                    + "WHERE ((symbol = ?) AND portfolio_id = ?);");
            stmt.setString(1, symbol);
            stmt.setInt(2, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                return rs.getInt(1);
            }
            return -1;
        } catch (SQLException ex) {
            System.out.println("Error finding this user's portfolio: ");
            ex.printStackTrace();
            return -1;
        }
    }

    // Gets all the stocks in a portfolio
    public static ResultSet getStocks(int id, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT symbol, total FROM bought "
                    + "WHERE (portfolio_id = ?) ORDER BY symbol ASC;");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs;
        } catch (SQLException ex) {
            System.out.println("Error finding this user's portfolio stocks: ");
            ex.printStackTrace();
            return null;
        }
    }

    // Gets the sum of most recent stock values of portfolio purchased stocks
    public static double estimateStockValue(int id, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT SUM(b.total * s.close) FROM bought b INNER JOIN "+
            "(SELECT symbol, timestamp, close FROM stock_data WHERE "+
            "(symbol, timestamp) IN (SELECT symbol, MAX(timestamp) "+
            "FROM stock_data GROUP BY symbol)) s ON b.symbol = s.symbol "+
            "WHERE b.portfolio_id = ?;");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                return rs.getDouble(1);
            }
            return 0;
        } catch (SQLException ex) {
            System.out.println("Error finding this user's portfolio stocks estimate: ");
            ex.printStackTrace();
            return -1;
        }
    }

    // Gets the all the stocks in a portfolio
    public static double estimatePortfolioValue(int id, Connection conn) {
        double bal = getBalance(id, conn);
        double val = estimateStockValue(id, conn);
        double realbal = 0;
        double realval = 0;
        if (bal > 0){
            realbal = bal;
        }
        if (val > 0){
            realval = val;
        }
        return realbal+realval;
    }

    // Creates a new bought tuple
    private static void createStockBought(int portfolioId, String symbol, int total, Connection conn){
        try {
            if (total > 0) {
                PreparedStatement stmt;
                stmt = conn.prepareStatement("INSERT INTO bought (portfolio_id, symbol, total) VALUES (?, ?, ?);");
                stmt.setInt(1, portfolioId);
                stmt.setString(2, symbol);
                stmt.setInt(3, total);
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println("Error creating bought stock for portfolio");
            ex.printStackTrace();
        }
    }

    // Updates a bought tuple. If new total is 0 or less, delete instead
    private static void updateStockBought(int portfolioId, String symbol, int newtotal, Connection conn){
        try {
            if (newtotal > 0) {
                PreparedStatement stmt;
                stmt = conn.prepareStatement("UPDATE bought SET total = ? WHERE (portfolio_id = ? AND symbol = ?);");
                stmt.setInt(1, newtotal);
                stmt.setInt(2, portfolioId);
                stmt.setString(3, symbol);
                stmt.executeUpdate();
            }
            else {
                PreparedStatement stmt;
                stmt = conn.prepareStatement("DELETE FROM bought WHERE (portfolio_id = ? AND symbol = ?);");
                stmt.setInt(1, portfolioId);
                stmt.setString(2, symbol);
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println("Error updating stock for portfolio");
            ex.printStackTrace();
        }
    }

    // Creates a buy stock transaction, and also updates the cash history, and the bought table
    public static void tradeStocksUpdate(int portfolioId, String symbol, String type, int quantity, Connection conn){
        double stockprice = Stocks.getCurrentStockPrice(symbol);
        double totalCost = stockprice*quantity;
        int totalStock = getTotalStock(portfolioId, symbol, conn);
        if (type.equals("buy") && totalStock > -1 && totalCost > -1){    
            double newbal = CashHistory.performCashTransaction(portfolioId, "withdraw", totalCost, conn);
            if(newbal >= 0){
                Transaction.buyStockTransaction(portfolioId, symbol, quantity, conn);
                updateStockBought(portfolioId, symbol, totalStock+quantity, conn);
            }
        }
        else if (type.equals("sell") && totalStock > -1 && totalCost > -1){
            CashHistory.performCashTransaction(portfolioId, "deposit", totalCost, conn);
            updateStockBought(portfolioId, symbol, totalStock-quantity, conn);
        }
    }

    //creates a buy stock transaction, and also updates the cash history, and the bought table
    public static void tradeStocksCreate(int portfolioId, String symbol, String type, int quantity, Connection conn){
        double stockprice = Stocks.getCurrentStockPrice(symbol);
        double totalCost = stockprice*quantity;
        int totalStock = getTotalStock(portfolioId, symbol, conn);
        if (type.equals("buy") && totalStock == -1 && stockprice > -1 && quantity > -1){ 
            Transaction.buyStockTransaction(portfolioId, symbol, quantity, conn);   
            double newbal = CashHistory.performCashTransaction(portfolioId, "withdraw", totalCost, conn);
            if(newbal >= 0){
                Transaction.buyStockTransaction(portfolioId, symbol, quantity, conn);
                createStockBought(portfolioId, symbol, quantity, conn);
            }
        }
    }

    //gets stock transactions for a portfolio
    public static ResultSet getPortfolioTransactions(int portfolioId, Connection conn){
        return Transaction.getTransactions(portfolioId, conn);
    }

    //adds stock list to database and the id tuples into the contains table
    public static void createStockListInPortfolio(int portfolio_id, String listName, String owner, String privacy, Connection conn){
        try {
            int newid = StockList.createStockList(owner, listName, privacy, conn);
            if (newid > -1){
                PreparedStatement stmt;
                stmt = conn.prepareStatement("INSERT INTO contains (stocklist_id, portfolio_id) VALUES (?, ?);");
                stmt.setInt(1, newid);
                stmt.setInt(2, portfolio_id);
                stmt.executeUpdate();
                System.out.println("Created a new stock list for this portfolio successfully for this user");
            }
        } catch (Exception ex) {
            System.out.println("Error creating a new stock list for this portfolio for this user");
            ex.printStackTrace();
        }
    }

    // adds stock list to contains table so that a portfolio contains it
    public static void addStockListToPortfolio(int portfolio_id, int stocklist_id, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("INSERT INTO contains (portfolio_id, stocklist_id) "
                    + "VALUES (?, ?);");
            stmt.setInt(1, portfolio_id);
            stmt.setInt(2, stocklist_id);
            stmt.executeUpdate();
            System.out.println("Added stock list to a portfolio successfully");            
        } catch (SQLException ex) {
            System.out.println("Error adding stock list to portfolio: " + ex.getMessage());
        }
    }

    //returns all stock lists for a given portfolio
    public static ResultSet getStockLists(int portfolioId, Connection conn){
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT stocklist_id, name, privacy FROM stock_list WHERE (stocklist_id IN (SELECT stocklist_id FROM contains WHERE portfolio_id = ?));");
            stmt.setInt(1, portfolioId);
            ResultSet rs = stmt.executeQuery();
            return rs;
        } catch (SQLException ex) {
            System.out.println("Error getting stock lists in this portfolio" + ex.getMessage());
            return null;
        }
    }

    // Delete the stock list from being contained by portfolio in the database
    public static void deleteStockListFromPortfolio(int portfolio_id, int stocklist_id, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("DELETE FROM contains "
                    + "WHERE (portfolio_id = ? AND stocklist_id = ?);");
            stmt.setInt(1, portfolio_id);
            stmt.setInt(2, stocklist_id);
            stmt.executeUpdate();
            System.out.println("Deleted stock list from portfolio successfully");            
        } catch (SQLException ex) {
            System.out.println("Error deleting stock list from portfolio: " + ex.getMessage());
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
            PreparedStatement stmt3;
            stmt3 = conn.prepareStatement("DELETE FROM contains "
                    + "WHERE (portfolio_id = ?);");
            stmt3.setInt(1, portfolio_id);
            stmt3.executeUpdate();
            System.out.println("Deleted portfolio successfully");            
        } catch (SQLException ex) {
            System.out.println("Error deleting portfolio: " + ex.getMessage());
        }
    }
}
