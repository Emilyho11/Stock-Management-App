package cs.toronto.edu.src;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Supplier;

public abstract class Table<T> {
    protected static DBHandler db;

    // Create object
    protected Table() {
    }

    public static void Initialize() {
        db = DBHandler.getInstance();
    }
    
    // Returns identifier for instance. (i.e "id = {ID}")
    protected abstract String getWhereIdentifier();

    // Getters and setters for PK
    public abstract String getKey();
    public abstract void setKey(String key);
    public abstract String toString();

    public abstract String getTableName();
    // Create entry
    public String createTable() {
        return "Table create";
    }

    // This method maps the columns from a ResultSet to the fields of a Table
    // instance, assuming the fields in the class have a prefix f_ and the
    // corresponding column names in the ResultSet do not.
    public static <T extends Table<T>> void createFromRS(Supplier<? extends T> constructor, ResultSet rs) {
        T table = constructor.get();

        Field[] fields = table.getClass().getDeclaredFields();
        
        try {
            // Insert the user to the database
            for (Field field : fields) {
                try {
                    // Parse the ones with 'f_' prefix
                    if (!field.getName().startsWith("f_")) continue;
                    String trueFieldName = field.getName().substring(2);

                    switch (field.getType().getName()) {
                        case "int":
                            field.set(table, rs.getInt(field.getName().substring(2)));
                            break;
                        case "java.lang.String":
                            field.set(table, rs.getString(field.getName().substring(2)));
                            break;
                        case "java.sql.Timestamp":
                            field.set(table, rs.getTimestamp(field.getName().substring(2)));
                            break;
                        default:
                            field.set(table, rs.getString(field.getName().substring(2)));
                            break;
                    }
                } catch (Exception e) {
                    System.out.println("Error getting field: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Error updating instance: " + e.getMessage());
        }
    }

    // Due to time constraints, we will not be abstracting this method
    // Each table will have their own any-num constructor
    public static <T extends Table<T>> T create(Supplier<? extends T> constructor, Object ... args) {
        T newClass = constructor.get();

        Field[] fields = newClass.getClass().getDeclaredFields();
        System.out.println("Fields: " + Arrays.toString(fields));

        assert fields.length == args.length;

        ArrayList<Field> validFields = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();

        // Convert args to string
        for (Object arg : args) {
            values.add("'" + arg.toString() + "'");
        }
        
        // Get fields with 'f_' prefix
        for (Field field : fields) {
            try {
                // Parse the ones with 'f_' prefix
                if (!field.getName().startsWith("f_")) continue;
                validFields.add(field);
            } catch (Exception e) {
                System.out.println("Error getting field: " + e.getMessage());
            }
        }

        try {
            // Insert the user to the database
            for (int i = 0; i < validFields.size(); i++) {
                Field field = validFields.get(i);
                try {
                    // Parse the ones with 'f_' prefix
                    if (!field.getName().startsWith("f_")) continue;
                    // field.set(newClass, args[i]);
                    setField(newClass, field.getName(), args[i]);
                } catch (Exception e) {
                }
            }
            
            StringBuilder sqlUpdate = new StringBuilder("INSERT INTO ");
            sqlUpdate.append(newClass.getTableName());
            sqlUpdate.append(" (");
            for (Field field : validFields) {
                sqlUpdate.append(field.getName().substring(2));
                sqlUpdate.append(", ");
            }
            sqlUpdate.setLength(sqlUpdate.length() - 2);

            sqlUpdate.append(") VALUES (");
            sqlUpdate.append(String.join(", ", values));
            sqlUpdate.append(") WHERE " + newClass.getWhereIdentifier() + ";");

            System.out.println(sqlUpdate.toString());
            int change = db.executeUpdate(sqlUpdate.toString());

            if (change > 0) {
                return newClass;
            }
        } catch (Exception e) {
            System.out.println("Error updating instance: " + e.getMessage());
        }

        return null;
    }

    protected static void setField(Object newClass, String fieldName, Object value) {
        try {
            Field field = newClass.getClass().getDeclaredField(fieldName);
            boolean accessible = field.canAccess(field);

            if (accessible) field.set(newClass, value);
            else { throw new IllegalAccessException("Field is not accessible"); }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // Handle the exception according to your needs
            e.printStackTrace();
        }
    }

    public static boolean clearTable(String tableName) {
        // Insert the user to the database
        String sqlDelete = "DELETE FROM " + tableName + ";";
        System.out.println(sqlDelete);
        try {
            db.executeUpdate(sqlDelete);
            return true;
        } catch (Exception e) {
            System.out.println("Error deleting instance: " + e.getMessage());
        }
        return false;
    }

    // Read
    public String read() {
        return "Table read";
    }

    // Updates the database entry using the current object
    public void update() {
        Field[] fields = this.getClass().getDeclaredFields();
        System.out.println("Fields: " + Arrays.toString(fields));

        StringBuilder sqlUpdate = new StringBuilder("UPDATE " + this.getTableName() + " SET ");

        for (Field field : fields) {
            try {
                // Parse the ones with 'f_' prefix
                if (!field.getName().startsWith("f_")) continue;
                String trueFieldName = field.getName().substring(2);
                sqlUpdate.append(trueFieldName + " = '" + field.get(this) + "' , ");
                System.out.println(trueFieldName + " = " + field.get(this));
            } catch (Exception e) {
                System.out.println("Error getting field: " + e.getMessage());
            }
        }

        // Strip last ", "
        sqlUpdate.setLength(sqlUpdate.length() - 2);

        // Update the database
        sqlUpdate.append(" WHERE " + getWhereIdentifier() + ";");

        System.out.println(sqlUpdate.toString());
        try {
            db.executeUpdate(sqlUpdate.toString());
            System.out.println("Updated successfully");
        } catch (Exception e) {
            System.out.println("Error updating instance: " + e.getMessage());
        }
    }


    // Delete
    public boolean delete() {
        // Insert the user to the database
        String sqlDelete = "DELETE FROM " + this.getTableName() + " WHERE " + getWhereIdentifier() + ";";
        System.out.println(sqlDelete);
        int deletes = 0;
        try {
            deletes = db.executeUpdate(sqlDelete);
            System.out.println("Deleted successfully");
        } catch (Exception e) {
            System.out.println("Error deleting instance: " + e.getMessage());
        }

        // Delete this
        setKey(null);
        return deletes > 0;
    }

    // Print all entries in the table based on table name. It should not have any parameters
    public static void printAll(String tableName) {
        try {
            
            String getAll = "SELECT * FROM " + tableName + ";";
            ResultSet rs = db.executeQuery(getAll);
            
            ResultSetMetaData metadata = rs.getMetaData();
            int columnCount = metadata.getColumnCount();  

            for (int i = 1; i <= columnCount; i++) {
                System.out.print(metadata.getColumnName(i) + "\t");      
            }
            System.out.println();

            while (rs.next()) {
                String row = "";
                for (int i = 1; i <= columnCount; i++)
                    row += rs.getString(i) + "\t";          
                System.out.println(row);
            }

        } catch (Exception e) {
            System.out.println("Error printing all entries: " + e.getMessage());
        }
    }

    // Get all rows based on a value in a column
    public static void printRowsFromValue(String tableName, String columnName) {
        try {
            String getAll = "SELECT " + columnName + " FROM " + tableName + ";";
            ResultSet rs = db.executeQuery(getAll);
            
            ResultSetMetaData metadata = rs.getMetaData();
            int columnCount = metadata.getColumnCount();  

            for (int i = 1; i <= columnCount; i++) {
                System.out.print(metadata.getColumnName(i) + "\t");      
            }
            System.out.println();

            while (rs.next()) {
                String row = "";
                for (int i = 1; i <= columnCount; i++)
                    row += rs.getString(i) + "\t";          
                System.out.println(row);
            }

        } catch (Exception e) {
            System.out.println("Error printing all entries: " + e.getMessage());
     }
    }
}

