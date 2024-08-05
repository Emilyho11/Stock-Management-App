package stocks_api.stocks_api.routes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import stocks_api.stocks_api.logic.src.Reviews;
import stocks_api.stocks_api.logic.src.StockList;
import stocks_api.stocks_api.logic.src.DBHandler;
import stocks_api.stocks_api.logic.src.ParserUtil;
import stocks_api.stocks_api.utils.BasicResponse;
import org.springframework.web.bind.annotation.CrossOrigin;

import stocks_api.stocks_api.logic.src.User;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/reviews", produces="application/json")
public class ReviewController {

    @GetMapping("/")
    public ArrayList<Reviews> getReviews() {
        try {
            String sqlQuery = "SELECT * FROM reviews";
            PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlQuery);
            ResultSet rs = preparedStatement.executeQuery();
            ArrayList<Reviews> reviews = new ArrayList<Reviews>();
            while (rs.next()){
                Reviews review = new Reviews(rs.getString("username"), rs.getInt("stock_list_id"), rs.getString("content"));
                reviews.add(review);
            }
            String result = ParserUtil.resultSetToJson(rs);
            return reviews;
            //return BasicResponse.ok(result);           
        } catch (Exception e) {
            e.printStackTrace();
            return null;
            //return BasicResponse.error("Failed to get reviews");
        }
    }

    @GetMapping("/{username}/{stock_list_id}")
    public BasicResponse getReview(@PathVariable String username, @PathVariable int stock_list_id) {
        try {
            String sqlQuery = "SELECT * FROM reviews WHERE username = ? AND stock_list_id = ?";
            PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlQuery);
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, stock_list_id);
            ResultSet rs = preparedStatement.executeQuery();
            String result = ParserUtil.resultSetToJson(rs);
            System.out.println("ResultSet");
            System.out.println(result);
            return BasicResponse.ok(result);            
        } catch (Exception e) {
            e.printStackTrace();
            return BasicResponse.error("Failed to get review by username and stock list id");
        }
    }

    @PostMapping("/")
    @ResponseBody
    public BasicResponse createReview(@RequestBody Reviews review) {
        // Check if the stockListId's privacy value is set to true, return because users cannot review the stock without it being public.
        if (StockList.isPrivate(review.getf_stock_list_id(), DBHandler.getInstance().getConnection())) {
            return BasicResponse.error("Stock list is private, cannot review");
        }
        try {
            String sqlQuery = "INSERT INTO reviews (username, stock_list_id, content) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlQuery);
            preparedStatement.setString(1, review.getf_username());
            preparedStatement.setInt(2, review.getf_stock_list_id());
            preparedStatement.setString(3, review.getf_content());
            preparedStatement.executeUpdate();
            return BasicResponse.ok("Review created successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return BasicResponse.error("Failed to create review");
        }
    }

    @PatchMapping("/{username}/{stock_list_id}")
    @ResponseBody
    public BasicResponse updateReview(@PathVariable String username, @PathVariable int stock_list_id, @RequestBody Reviews review) {
        try {
            String sqlQuery = "UPDATE reviews SET content = ? WHERE username = ? AND stock_list_id = ?";
            PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlQuery);
            preparedStatement.setString(1, review.getf_content());
            preparedStatement.setString(2, username);
            preparedStatement.setInt(3, stock_list_id);
            preparedStatement.executeUpdate();
            return BasicResponse.ok("Review updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return BasicResponse.error("Failed to update review");
        }
    }

    @DeleteMapping("/{username}/{stock_list_id}")
    @ResponseBody
    public BasicResponse deleteReview(@PathVariable String username, @PathVariable int stock_list_id) {
        try {
            String sqlQuery = "DELETE FROM reviews WHERE username = ? AND stock_list_id = ?";
            PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlQuery);
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, stock_list_id);
            preparedStatement.executeUpdate();
            return BasicResponse.ok("Review deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return BasicResponse.error("Failed to delete review");
        }
    }

    @GetMapping("/stock/{stock_list_id}")
    @ResponseBody
    public BasicResponse getReviewsByStockListId(@PathVariable int stock_list_id) {
        try {
            String sqlQuery = "SELECT * FROM reviews WHERE stock_list_id = ?";
            PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlQuery);
            preparedStatement.setInt(1, stock_list_id);
            ResultSet rs = preparedStatement.executeQuery();
            String result = ParserUtil.resultSetToJson(rs);
            System.out.println("ResultSet");
            System.out.println(result);
            return BasicResponse.ok(result);            
            
        } catch (Exception e) {
            // output error message
            return BasicResponse.error("Failed to get reviews by stock list id");
        }
    }

    @GetMapping("/user/{username}")
    @ResponseBody
    public BasicResponse getReviewsByUsername(@PathVariable String username) {
        try {
            String sqlQuery = "SELECT * FROM reviews WHERE username = ?";
            PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlQuery);
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            String result = ParserUtil.resultSetToJson(rs);
            System.out.println("ResultSet");
            System.out.println(result);
            return BasicResponse.ok(result);            
            
        } catch (Exception e) {
            // output error message
            return BasicResponse.error("Failed to get reviews by username");
        }
    }
}