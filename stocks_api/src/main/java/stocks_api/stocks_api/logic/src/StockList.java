package stocks_api.stocks_api.logic.src;

import java.sql.Statement;
import java.sql.ResultSet;

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
