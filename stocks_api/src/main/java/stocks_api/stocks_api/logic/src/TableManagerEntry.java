package stocks_api.stocks_api.logic.src;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.function.Supplier;

public class TableManagerEntry <T extends Table<T>> {
    private static DBHandler dbHandler;
    private Supplier <? extends T> constructor;
    private String tableName;
    private ArrayList<T> rows;
    
    public void fetchTable() {
        
        String query = "SELECT * FROM " + tableName + ";";
        try {
        ResultSet resultSet = dbHandler.executeQuery(query);

        while (resultSet.next()) {
            Table.createFromRS(constructor, resultSet);
        }

        } catch (Exception e) {
            System.out.println("Error fetching table: " + tableName);
            e.printStackTrace();
        }
    }

    public TableManagerEntry(String tableName, Supplier <? extends T> constructor) {
        TableManagerEntry.dbHandler = DBHandler.getInstance();
        this.rows = new ArrayList<>();
        this.constructor = constructor;
        this.tableName = tableName;
    }

    // Function that will return the array list of rows based on table name
    public ArrayList<T> getRows() {
        return rows;
    }
}
