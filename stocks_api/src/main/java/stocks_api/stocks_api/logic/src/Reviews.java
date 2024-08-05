package stocks_api.stocks_api.logic.src;
import java.sql.ResultSet;

import stocks_api.stocks_api.logic.src.Table;

public class Reviews extends Table{
    public static final String TABLE_NAME = "reviews";
    public String f_username;
    public int f_stock_list_id;
    public String f_content;

    public Reviews(String username, int id, String content) {
        f_username = username;
        f_stock_list_id = id;
        f_content = content;
    };

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getKey() {
        return f_username + ", " + f_stock_list_id;
    }

    @Override
    public void setKey(String key) {
        if (key == null) {
            this.f_username = null;
            this.f_stock_list_id = 0;
            return;
        }
        String[] keys = key.split(", ");
        this.f_username = keys[0];
        this.f_stock_list_id = Integer.parseInt(keys[1]);
    }

    @Override
    protected String getWhereIdentifier() {
        return "username = '" + f_username + "' AND stock_list_id = '" + f_stock_list_id + "'";
    }

    @Override
    public String toString() {
        return "{username: " + this.f_username + ", stock_list_id: " + this.f_stock_list_id + ", content: " + this.f_content + "}";
    }

    public String getf_username() {
        return f_username;
    }

    public int getf_stock_list_id() {
        return f_stock_list_id;
    }

    public String getf_content() {
        return f_content;
    }

    public void setf_username(String f_username) {
        this.f_username = f_username;
    }

    public void setf_stock_list_id(int f_stock_list_id) {
        this.f_stock_list_id = f_stock_list_id;
    }

    public void setf_content(String f_content) {
        this.f_content = f_content;
    }

    // Find reviews by stock list id
    public static ResultSet findByf_stock_list_id(int stock_list_id) {
        try {
            ResultSet rs = Table.db.executeQuery("SELECT * FROM reviews WHERE stock_list_id = '" + stock_list_id + "';");
            return rs;
        }
        catch (Exception e) {
            System.out.println("Error finding Reviews: " + e.getMessage());
            return null;
        }
    }
    
    // Find reviews by username
    public static ResultSet findByf_username(String username) {
        try {
            ResultSet rs = Table.db.executeQuery("SELECT * FROM reviews WHERE username = '" + username + "';");
            return rs;
        }
        catch (Exception e) {
            System.out.println("Error finding Reviews: " + e.getMessage());
            return null;
        }
    }
}