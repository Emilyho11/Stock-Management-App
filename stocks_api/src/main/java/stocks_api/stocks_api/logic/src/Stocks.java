package stocks_api.stocks_api.logic.src;
import java.io.ObjectInputStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.*;
import java.lang.reflect.Field;

public class Stocks extends Table<Stocks> {
    public static final String TABLE_NAME = "stocks";
    protected String f_symbol;
    protected Double f_COV;

    public Stocks() {}

    // private Stocks(String symbol, Double COV) {
    //     this.f_symbol = symbol;
    //     this.f_COV = COV;
    // }

    // public static Stocks create(String symbol, Double COV) {
    //     try {
    //         String values = String.format("('%s', %f)", symbol, COV);
    //         System.out.println("INSERT INTO " + tableName + "(symbol, cov) VALUES " + values + ";");
    //         int change = Table.db.executeUpdate("INSERT INTO " + tableName + "(symbol, cov) VALUES " + values + ";");
    //         // Ensure the stock was created
    //         if (change > 0) {
    //             return new Stocks(symbol, COV);
    //         }

    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         System.out.println("Error creating stock: " + e.getMessage());
    //     }

    //     return null;
    // }

    // User user = User.create();
    // user.setName("John");
    // user.setID("123");
    // user.update();

    // user.update("name", "John");
    // @Override
    // public void update() {
    //     String sqlUpdate = "UPDATE stocks SET symbol = '" + symbol + "', cov = " + COV + " WHERE symbol = '" + symbol + "';";
    //     try {
    //         Table.db.executeUpdate(sqlUpdate);
    //         System.out.println("Stocks updated successfully");
    //     } catch (Exception e) {
    //         System.out.println("Error updating Stocks: " + e.getMessage());
    //     }
    // }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }


    @Override
    protected String getWhereIdentifier() {
        return "symbol = '" + f_symbol + "'";
    }

    @Override
    public String getKey() {
        return f_symbol;
    }

    @Override
    public void setKey(String key) {
        if (key == null) {
            this.f_symbol = null;
            return;
        }
        this.f_symbol = key;
    }

    public String getSymbol() {
        return f_symbol;
    }

    public Double getCOV() {
        return f_COV;
    }

    public void setSymbol(String symbol) {
        this.f_symbol = symbol;
    }

    public void setCOV(Double COV) {
        this.f_COV = COV;
    }

    // public static Stocks findBySymbol(Statement stmt, ResultSet rs, String symbol) {
    //     Stocks stocks = null;
    //     try {
    //         String sqlQuery = "SELECT * FROM stocks WHERE symbol = '" + symbol + "';";
    //         rs = stmt.executeQuery(sqlQuery);
    //         if (rs.next()) {
    //             stocks = new Stocks(rs.getString("symbol"), rs.getDouble("cov"));
    //         }
    //     } catch (Exception e) {
    //         System.out.println("Error finding Stocks: " + e.getMessage());
    //     }
    //     return stocks;
    // }


    public static void printAllStocks(Statement stmt, ResultSet rs) {
        try {
            String sqlQuery = "SELECT * FROM stocks;";
            rs = stmt.executeQuery(sqlQuery);
            System.out.println("Table Stocks contains the following tuples:\nsymbol \tCOV");
            while (rs.next()) {
                String symbol = rs.getString("symbol");
                Double COV = rs.getDouble("cov");
                System.out.println(symbol + ", " + COV);
            }
        } catch (Exception e) {
            System.out.println("Error printing Stocks: " + e.getMessage());
        }
    }

    // public void updateCOV(Statement stmt, Double COV) {
    //     this.COV = COV;
    //     String sqlUpdate = "UPDATE stocks SET cov = '" + COV + "' WHERE symbol = '" + f_symbol + "';";
    //     try {
    //         stmt.executeUpdate(sqlUpdate);
    //         System.out.println("Stocks updated successfully");
    //     } catch (Exception e) {
    //         System.out.println("Error updating Stocks: " + e.getMessage());
    //     }
    // }

    // public void updateSymbol(Statement stmt, String f_symbol) {
    //     String sqlUpdate = "UPDATE stocks SET f_symbol = '" + symbol + "' WHERE symbol = '" + this.symbol + "';";
    //     try {
    //         this.symbol = symbol;
    //         stmt.executeUpdate(sqlUpdate);
    //         System.out.println("Stocks updated successfully");
    //     } catch (Exception e) {
    //         System.out.println("Error updating Stocks: " + e.getMessage());
    //     }
    // }
}
