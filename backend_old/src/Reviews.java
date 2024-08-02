package cs.toronto.edu.src;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class Reviews extends Table{
    public static final String TABLE_NAME = "reviews";
    protected String f_username;
    protected int f_stock_list_id;
    protected String f_content;

    public Reviews() {}

    private Reviews(String f_username, int f_stock_list_id, String f_content) {
        this.f_username = f_username;
        this.f_stock_list_id = f_stock_list_id;
        this.f_content = f_content;
    }

    // @Override
    // public String getTableName() {
    //     return tableName;
    // }

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

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    // public static Reviews findByf_usernameAndf_stock_list_id(Statement stmt, ResultSet rs, String f_username, int f_stock_list_id) {
    //     Reviews review = null;
    //     try {
    //         String sqlQuery = "SELECT * FROM reviews WHERE f_username = '" + f_username + "' AND stock_list_id = '" + f_stock_list_id + "';";
    //         rs = stmt.executeQuery(sqlQuery);
    //         if (rs.next()) {
    //             review = new Reviews(rs.getString("f_username"), rs.getInt("stock_list_id"), rs.getString("f_content"));
    //         }
    //     } catch (Exception e) {
    //         System.out.println("Error finding review: " + e.getMessage());
    //     }
    //     return review;
    // }

    // public static ArrayList<Reviews> findByf_username(Statement stmt, ResultSet rs, String f_username) {
    //     ArrayList<Reviews> reviews = new ArrayList<Reviews>();
    //     try {
    //         String sqlQuery = "SELECT * FROM reviews WHERE f_username = '" + f_username + "';";
    //         rs = stmt.executeQuery(sqlQuery);
    //         while (rs.next()) {
    //             reviews.add(new Reviews(rs.getString("f_username"), rs.getString("stock_list_id"), rs.getString("f_content")));
    //         }
    //         // Print list of reviews
    //         for (Reviews review : reviews) {
    //             System.out.println("f_username: " + review.getf_username() + ", Stock List ID: " + review.getf_stock_list_id() + ", f_content: " + review.getf_content());
    //         }
    //     } catch (Exception e) {
    //         System.out.println("Error finding review: " + e.getMessage());
    //     }
    //     return reviews;
    // }

    // public static ArrayList<Reviews> findByf_stock_list_id(Statement stmt, ResultSet rs, int f_stock_list_id) {
    //     ArrayList<Reviews> reviews = new ArrayList<Reviews>();
    //     try {
    //         String sqlQuery = "SELECT * FROM reviews WHERE stock_list_id = '" + f_stock_list_id + "';";
    //         rs = stmt.executeQuery(sqlQuery);
    //         while (rs.next()) {
    //             reviews.add(new Reviews(rs.getString("f_username"), rs.getInt("stock_list_id"), rs.getString("f_content")));
    //         }
    //         // Print list of reviews
    //         for (Reviews review : reviews) {
    //             System.out.println("f_username: " + review.getf_username() + ", Stock List ID: " + review.getf_stock_list_id() + ", f_content: " + review.getf_content());
    //         }
    //     } catch (Exception e) {
    //         System.out.println("Error finding review: " + e.getMessage());
    //     }
    //     return reviews;
    // }

    // public static ArrayList<Reviews> findAll(Statement stmt, ResultSet rs) {
    //     ArrayList<Reviews> reviews = new ArrayList<Reviews>();
    //     try {
    //         String sqlQuery = "SELECT * FROM reviews;";
    //         rs = stmt.executeQuery(sqlQuery);
    //         while (rs.next()) {
    //             reviews.add(new Reviews(rs.getString("f_username"), rs.getString("stock_list_id"), rs.getString("f_content")));
    //         }
    //         // Print list of reviews
    //         for (Reviews review : reviews) {
    //             System.out.println("f_username: " + review.getf_username() + ", Stock List ID: " + review.getf_stock_list_id() + ", f_content: " + review.getf_content());
    //         }
    //     } catch (Exception e) {
    //         System.out.println("Error finding review: " + e.getMessage());
    //     }
    //     return reviews;
    // }

    // public static void printAllReviews(Statement stmt, ResultSet rs) {
    //     try {
    //         String sqlQuery = "SELECT * FROM reviews;";
    //         rs = stmt.executeQuery(sqlQuery);
    //         System.out.println("Table Reviews contains the following tuples:\nf_username \tstock_list_id \tf_content");
    //         while (rs.next()) {
    //             String f_username = rs.getString("f_username");
    //             String f_stock_list_id = rs.getString("stock_list_id");
    //             String f_content = rs.getString("f_content");
    //             System.out.println(f_username + ", " + f_stock_list_id + ", " + f_content);
    //         }
    //     } catch (Exception e) {
    //         System.out.println("Error printing Reviews: " + e.getMessage());
    //     }
    // }

    // public void updateReview(Statement stmt) {
    //     String sqlUpdate = "UPDATE reviews SET f_content = '" + this.f_content + "' WHERE f_username = '" + this.f_username + "' AND stock_list_id = '" + this.f_stock_list_id + "';";
    //     try {
    //         stmt.executeUpdate(sqlUpdate);
    //         System.out.println("Review updated successfully");
    //     } catch (Exception e) {
    //         System.out.println("Error updating review: " + e.getMessage());
    //     }
    // }
}
