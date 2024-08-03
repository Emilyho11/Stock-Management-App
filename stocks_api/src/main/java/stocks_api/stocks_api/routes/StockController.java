package stocks_api.stocks_api.routes;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

import stocks_api.stocks_api.logic.src.Stocks;
import stocks_api.stocks_api.logic.src.StockData;
import stocks_api.stocks_api.logic.src.User;
import stocks_api.stocks_api.logic.src.DBHandler;
import stocks_api.stocks_api.logic.src.ParserUtil;
import stocks_api.stocks_api.utils.BasicResponse;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.StringBuilder;

@RestController
@RequestMapping(value = "/stocks", produces="application/json")
public class StockController {

    @GetMapping("/")
    @ResponseBody
    public BasicResponse getAllStocks() {
        try {
            ResultSet rs = DBHandler.getInstance().executeQuery("SELECT * FROM stocks;");
            String result = ParserUtil.resultSetToJson(rs);
            System.out.println("ResultSet");
            System.out.println(result);
            return BasicResponse.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BasicResponse.ok("Failed");
    }

    @GetMapping("/{symbol}")
    @ResponseBody
    public BasicResponse getStock(@PathVariable String symbol) {
        try {
            String sqlQuery = "SELECT * FROM stocks WHERE symbol = ?";
            PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlQuery);
            preparedStatement.setString(1, symbol);
            ResultSet rs = preparedStatement.executeQuery();
            String result = ParserUtil.resultSetToJson(rs);
            System.out.println("ResultSet");
            System.out.println(result);
            return BasicResponse.ok(result);            
            
        } catch (Exception e) {
            e.printStackTrace();
            // output error message
            return BasicResponse.ok("Failed");
        }
    }


    @PostMapping("/")
    @ResponseBody
    public BasicResponse createStock(@RequestBody Stocks user) {
        try {
            // INSERT INTO stocks (symbol, cov) VALUES ('AAPL', 0.5);
            String sqlInsert = "INSERT INTO stocks (symbol, cov) VALUES (?, ?)";
            PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlInsert);
            preparedStatement.setString(1, user.getSymbol());
            preparedStatement.setDouble(2, user.getCOV());
            preparedStatement.executeUpdate();
            return BasicResponse.ok("Stock created successfully");
        } catch (Exception e) {
            e.printStackTrace();
            // output error message
            return BasicResponse.ok("Failed.");
        }
    }

    @PatchMapping("/{symbol}")
    @ResponseBody
    public BasicResponse updateStock(@PathVariable String symbol, @RequestBody Stocks stock) {
        try {
            StringBuilder fields = new StringBuilder("");

            if (stock.getCOV() != null) {
                fields.append("cov = " + stock.getCOV());
            }

            if (fields.length() == 0) {
                return BasicResponse.ok("No fields to update");
            }

            String sqlUpdate = "UPDATE stocks SET " + fields.toString() + " WHERE symbol = ?";
            
            PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlUpdate);
            preparedStatement.setString(1, symbol);

            preparedStatement.executeUpdate();
            
            return BasicResponse.ok("Updated Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            // output error message
            return BasicResponse.ok("Failed.");
        }
    }

    @DeleteMapping("/{symbol}")
    @ResponseBody
    public BasicResponse deleteStock(@PathVariable String symbol) {
        try {   
            String sqlDelete = "DELETE FROM stocks WHERE symbol = ?";
            PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlDelete);
            preparedStatement.setString(1, symbol);
            preparedStatement.executeUpdate();
            return BasicResponse.ok("Stock deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            // output error message
            return BasicResponse.ok("Failed.");
        }
    }

    // ----- STOCK DATA -----

    
    @GetMapping("/{symbol}/data")
    @ResponseBody
    public BasicResponse getStockData(@PathVariable String symbol,
        @RequestParam(required = false) String start_date, 
        @RequestParam(required = false) String end_date) {
        try {
            StringBuilder sqlQuery = new StringBuilder("SELECT * FROM stock_data WHERE symbol = ? ");

            if (start_date != null) {
                sqlQuery.append("AND timestamp >= '" + start_date + "' ");
            }

            if (end_date != null) {
                sqlQuery.append("AND timestamp <= '" + end_date + "' ");
            }
            
            PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlQuery.toString());
            preparedStatement.setString(1, symbol);
            ResultSet rs = preparedStatement.executeQuery();
            String result = ParserUtil.resultSetToJson(rs);
            System.out.println("ResultSet");
            System.out.println(result);
            return BasicResponse.ok(result);            
            
        } catch (Exception e) {
            e.printStackTrace();
            // output error message
            return BasicResponse.ok("Failed");
        }
    }

    @PostMapping("/{symbol}/data")
    @ResponseBody
    public BasicResponse createStockData(@PathVariable String symbol, @RequestBody StockData stock) {
        try {
            // Ensure all fields are present
            if (stock.f_symbol == null || stock.f_timestamp == null) {
                return BasicResponse.ok("Missing fields");
            }

            String sqlInsert = "INSERT INTO stock_data (symbol, timestamp, open, close, high, low, volume) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlInsert);
            preparedStatement.setString(1, symbol);
            preparedStatement.setString(2, stock.f_timestamp);
            preparedStatement.setDouble(3, stock.f_open);
            preparedStatement.setDouble(4, stock.f_close);
            preparedStatement.setDouble(5, stock.f_high);
            preparedStatement.setDouble(6, stock.f_low);
            preparedStatement.setDouble(7, stock.f_volume);
            preparedStatement.executeUpdate();
            return BasicResponse.ok("Stock data created successfully");
        } catch (Exception e) {
            e.printStackTrace();
            // output error message
            return BasicResponse.ok("Failed.");
        }
    }

    @PatchMapping("/{symbol}/data/{timestamp}")
    @ResponseBody
    public BasicResponse updateStockData(@PathVariable String symbol, 
    @PathVariable String timestamp, @RequestBody StockData stock) {
        try {
            StringBuilder fields = new StringBuilder("");

            if (stock.f_symbol != null) {
                fields.append("symbol = '" + stock.f_symbol + "', ");
            }

            if (stock.f_timestamp != null) {
                fields.append("timestamp = '" + stock.f_timestamp + "', ");
            }
            
            if (fields.length() == 0) {
                return BasicResponse.ok("No fields to update");
            }

            fields.append("open = " + stock.f_open + ", ");
            fields.append("close = " + stock.f_close + ", ");
            fields.append("high = " + stock.f_high + ", ");
            fields.append("low = " + stock.f_low + ", ");
            fields.append("volume = " + stock.f_volume + ", ");


            String sqlUpdate = "UPDATE stock_data SET " + fields.toString() + " WHERE symbol = ? AND timestamp = ?";
            
            PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlUpdate);
            preparedStatement.setString(1, symbol);
            preparedStatement.setString(2, timestamp);

            preparedStatement.executeUpdate();
            
            return BasicResponse.ok("Updated Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            // output error message
            return BasicResponse.ok("Failed.");
        }
    }

    @DeleteMapping("/{symbol}/data/{timestamp}")
    @ResponseBody
    public BasicResponse deleteStockData(@PathVariable String symbol, @PathVariable String timestamp) {
        try {   
            String sqlDelete = "DELETE FROM stock_data WHERE symbol = ? AND timestamp = ?";
            PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlDelete);
            preparedStatement.setString(1, symbol);
            preparedStatement.setString(2, timestamp);
            preparedStatement.executeUpdate();
            return BasicResponse.ok("Stock data deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            // output error message
            return BasicResponse.ok("Failed.");
        }
    }

    
}
