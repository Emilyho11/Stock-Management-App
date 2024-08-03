package stocks_api.stocks_api.logic.src;
import java.io.ObjectInputStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.*;
import java.lang.reflect.Field;

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
}
