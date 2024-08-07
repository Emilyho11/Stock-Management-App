package stocks_api.stocks_api.logic.src;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBHandler {
    private static Connection conn = null;
    private static Statement stmt = null;
    private static final DBHandler instance = new DBHandler();
    public static void Initialize() throws ClassNotFoundException, SQLException {
        // 
        // Register the PostgreSQL driver
        //
        //
        Class.forName("org.postgresql.Driver");

        //
        // Connect to the database
        //
        //
        DBHandler.conn = DriverManager.getConnection("jdbc:postgresql://34.130.14.35:5432/mydb", "postgres", "postgres");
        System.out.println("Opened database successfully"); 

        //
        // Create a statement object
        //
        //
        DBHandler.stmt = conn.createStatement();
    }

    // Prevent external instantiations
    private DBHandler(){}

    public Connection getConnection() {
        return conn;
    }

    public static DBHandler getInstance() {
        return instance;
    }

    public ResultSet executeQuery(String query) throws SQLException {
        // Statement stmt = DBHandler.conn.createStatement();
        ResultSet results = stmt.executeQuery(query);
        return results;
    }

    public int executeUpdate(String query) throws SQLException {
        // Statement stmt = DBHandler.conn.createStatement();
        return stmt.executeUpdate(query);
    }

    public static void Close() throws SQLException {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
    }
}
