package cs.toronto.edu.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;


public class TableManager {
    private static TableManager instance;
    private final HashMap<String, TableManagerEntry> tables = new HashMap<>();

    private TableManager() {}

    public static TableManager getInstance() {
        if (instance == null) {
            instance = new TableManager();
            Table.Initialize();
        }
        return instance;
    }
    
    // Add entry to the table and then fetch the table
    public <T extends Table<T>> void addTable(String tableName, Supplier<? extends T> constructor) {
        TableManagerEntry<T> tableEntry = new TableManagerEntry<>(tableName, constructor);
        tableEntry.fetchTable();
        tables.put(tableName, tableEntry);
    }

    // Function to return table manager entry based on given name.
    public TableManagerEntry getTable(String tableName) {
        return tables.get(tableName);
    }
}