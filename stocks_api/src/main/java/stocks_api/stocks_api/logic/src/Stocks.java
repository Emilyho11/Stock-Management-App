package stocks_api.stocks_api.logic.src;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Stocks extends Table<Stocks> {
    public static final String TABLE_NAME = "stocks";
    public String f_symbol;
    public Double f_COV;

    public Stocks() {}

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

    public static double getCurrentStockPrice(String symbol) {
        try {
            String sqlQuery = "SELECT close FROM stock_data WHERE (symbol = ?) ORDER BY timestamp DESC FETCH FIRST 1 ROW ONLY;";
            PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlQuery);
            preparedStatement.setString(1, symbol);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()){
                return rs.getDouble(1);
            }
            return -1;            
            
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
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
