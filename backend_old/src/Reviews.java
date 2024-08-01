package backend_old.src;

public class Reviews {
    private String username;
    private String stockListId;
    private String content;

    public Reviews(String username, String stockListId, String content) {
        this.username = username;
        this.stockListId = stockListId;
        this.content = content;
    }

    public String getUsername() {
        return username;
    }
    
    public String getStockListId() {
        return stockListId;
    }

    public String getContent() {
        return content;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setStockListId(String stockListId) {
        this.stockListId = stockListId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void insertReview(Statement stmt) {
        // Insert the user to the database
        String sqlInsert = "INSERT INTO reviews (username, stock_list_id, content) " +
            "VALUES ('" + username + "', '" + stockListId + "', '" + content + "');";
        
            try {
                stmt.executeUpdate(sqlInsert);
                System.out.println("Review inserted successfully");
        } catch (Exception e) {
            System.out.println("Error inserting review: " + e.getMessage());
        }
    }

    public void deleteReview(Statement stmt) {
        // Delete the user from the database
        String sqlDelete = "DELETE FROM reviews WHERE username = '" + username + "' AND stock_list_id = '" + stockListId + "';";
        try {
            stmt.executeUpdate(sqlDelete);
            System.out.println("Review deleted successfully");
        } catch (Exception e) {
            System.out.println("Error deleting review: " + e.getMessage());
        }
    }

    public static Reviews findByUsernameAndStockListId(Statement stmt, ResultSet rs, String username, String stockListId) {
        Reviews review = null;
        try {
            String sqlQuery = "SELECT * FROM reviews WHERE username = '" + username + "' AND stock_list_id = '" + stockListId + "';";
            rs = stmt.executeQuery(sqlQuery);
            if (rs.next()) {
                review = new Reviews(rs.getString("username"), rs.getString("stock_list_id"), rs.getString("content"));
            }
        } catch (Exception e) {
            System.out.println("Error finding review: " + e.getMessage());
        }
        return review;
    }

    public static ArrayList<Reviews> findByUsername(Statement stmt, ResultSet rs, String username) {
        ArrayList<Reviews> reviews = new ArrayList<Reviews>();
        try {
            String sqlQuery = "SELECT * FROM reviews WHERE username = '" + username + "';";
            rs = stmt.executeQuery(sqlQuery);
            while (rs.next()) {
                reviews.add(new Reviews(rs.getString("username"), rs.getString("stock_list_id"), rs.getString("content")));
            }
        } catch (Exception e) {
            System.out.println("Error finding review: " + e.getMessage());
        }
        return reviews;
    }

    public static ArrayList<Reviews> findByStockListId(Statement stmt, ResultSet rs, String stockListId) {
        ArrayList<Reviews> reviews = new ArrayList<Reviews>();
        try {
            String sqlQuery = "SELECT * FROM reviews WHERE stock_list_id = '" + stockListId + "';";
            rs = stmt.executeQuery(sqlQuery);
            while (rs.next()) {
                reviews.add(new Reviews(rs.getString("username"), rs.getString("stock_list_id"), rs.getString("content")));
            }
        } catch (Exception e) {
            System.out.println("Error finding review: " + e.getMessage());
        }
        return reviews;
    }

    public static ArrayList<Reviews> findAll(Statement stmt, ResultSet rs) {
        ArrayList<Reviews> reviews = new ArrayList<Reviews>();
        try {
            String sqlQuery = "SELECT * FROM reviews;";
            rs = stmt.executeQuery(sqlQuery);
            while (rs.next()) {
                reviews.add(new Reviews(rs.getString("username"), rs.getString("stock_list_id"), rs.getString("content")));
            }
        } catch (Exception e) {
            System.out.println("Error finding review: " + e.getMessage());
        }
        return reviews;
    }

    public static void printAllReviews(Statement stmt, ResultSet rs) {
        ArrayList<Reviews> reviews = findAll(stmt, rs);
        System.out.println("Table reviews contains the following tuples:\nusername \tstock_list_id \tcontent");
        for (Reviews review : reviews) {
            System.out.println(review.getUsername() + ", " + review.getStockListId() + ", " + review.getContent());
        }
    }

    public void updateReview(Statement stmt, String content) {
        this.content = content;
        String sqlUpdate = "UPDATE reviews SET content = '" + content + "' WHERE username = '" + username + "' AND stock_list_id = '" + stockListId + "';";
        try {
            stmt.executeUpdate(sqlUpdate);
            System.out.println("Review updated successfully");
        } catch (Exception e) {
            System.out.println("Error updating review: " + e.getMessage());
        }
    }
}
