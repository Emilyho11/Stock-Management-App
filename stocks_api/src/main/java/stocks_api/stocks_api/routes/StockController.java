package stocks_api.stocks_api.routes;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;

import stocks_api.stocks_api.logic.src.Stocks;
import stocks_api.stocks_api.logic.src.StockData;
import stocks_api.stocks_api.logic.src.User;
import stocks_api.stocks_api.logic.src.DBHandler;
import stocks_api.stocks_api.logic.src.ParserUtil;
import stocks_api.stocks_api.utils.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.StringBuilder;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/stocks", produces="application/json")
public class StockController {

    // Get all stocks
    @GetMapping("/")
    public ArrayList<Stocks> getAllStocks() {
        try {
            String sqlQuery = "SELECT symbol, cov FROM stocks;";
            PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlQuery.toString());
            ResultSet rs = preparedStatement.executeQuery();
            ArrayList<Stocks> stocks = new ArrayList<Stocks>();
            while (rs.next()) {
                Stocks stock = new Stocks();
                stock.setSymbol(rs.getString("symbol"));
                stock.setCOV(rs.getDouble("cov"));
                stocks.add(stock);
            }
            return stocks;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get a specific stock
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

    // Get a stocks current COV
    @GetMapping("/COV/{symbol}")
    @ResponseBody
    public double getStockCOV(@PathVariable String symbol) {
        try {
            String sqlQuery = "SELECT cov FROM stocks WHERE symbol = ?";
            PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlQuery);
            preparedStatement.setString(1, symbol);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()){
                return rs.getDouble("cov");
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            // output error message
            return -1;
        }
    }

    // Get a stocks current price
    @GetMapping("/current/{symbol}")
    @ResponseBody
    public double getStockPrice(@PathVariable String symbol) {
        try {
            return Stocks.getCurrentStockPrice(symbol);
        } catch (Exception e) {
            e.printStackTrace();
            // output error message
            return -1;
        }
    }

    // This function inserts a stock into stocks table, but updates cov if the symbol already exists
    @PostMapping("/")
    @ResponseBody
    public BasicResponse insertStock(@RequestBody Stocks stock) {
        try {
            Stocks.insertAndUpdateStock(stock.getSymbol());
            return BasicResponse.ok("Stock created successfully");
        } catch (Exception e) {
            e.printStackTrace();
            // output error message
            return BasicResponse.ok("Failed.");
        }
    }

    // Update COV of stocks table based on symbol
    @PatchMapping("/")
    @ResponseBody
    public BasicResponse updateStock(@RequestBody Stocks stock) {
        try {
            Stocks.updateCOV(stock.getSymbol());
            return BasicResponse.ok("Updated COV Successfully!");
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

    
    // Api that checks if a stock exists
    @GetMapping("/exists/{symbol}")
    public Boolean stockExists(@PathVariable String symbol) {
        try {
            if (Stocks.symbolExists(symbol)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ----- STOCK DATA -----

    // Get all stock data of a company by symbol
    @GetMapping("/{symbol}/data")
    @ResponseBody
    public DateListResponse getStockData(@PathVariable String symbol,
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

            // SELECT MIN(timestamp), MAX(timestamp) FROM stock_data WHERE symbol = 'VFC'
            String sqlMinMax = "SELECT MIN(timestamp) AS min, MAX(timestamp) AS max FROM stock_data WHERE symbol = ?";
            PreparedStatement preparedStatementMinMax = DBHandler.getInstance().getConnection().prepareStatement(sqlMinMax);
            preparedStatementMinMax.setString(1, symbol);
            ResultSet rsMinMax = preparedStatementMinMax.executeQuery();
            String min = "";
            String max = "";
            while (rsMinMax.next()) {
                min = rsMinMax.getString("min");
                max = rsMinMax.getString("max");
            }
            
            PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlQuery.toString());
            preparedStatement.setString(1, symbol);
            ResultSet rs = preparedStatement.executeQuery();
            ArrayList<StockData> stockData = new ArrayList<StockData>();
            while (rs.next()) {
                StockData stock = new StockData();
                // stock.f_symbol = rs.getString("symbol");
                stock.f_timestamp = rs.getString("timestamp");
                stock.f_open = rs.getDouble("open");
                stock.f_close = rs.getDouble("close");
                stock.f_high = rs.getDouble("high");
                stock.f_low = rs.getDouble("low");
                stock.f_volume = rs.getDouble("volume");
                stockData.add(stock);
            }
            return new DateListResponse<>("Success", stockData, min, max);
            
        } catch (Exception e) {
            e.printStackTrace();
            // output error message
            return null;
        }
    }

    // Insert stock data into stock_data table
    @PostMapping("/data")
    @ResponseBody
    public BasicResponse createStockData(@RequestBody StockData stock) {
        try {
            // Ensure all fields are present
            if (stock.f_symbol == null) {
                return BasicResponse.ok("Missing fields");
            }

            // Insert data into stock_data table
            String sqlInsert = "INSERT INTO stock_data (symbol, timestamp, open, close, high, low, volume) VALUES (?, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlInsert);
            preparedStatement.setString(1, stock.f_symbol);
            preparedStatement.setDouble(2, stock.f_open);
            preparedStatement.setDouble(3, stock.f_close);
            preparedStatement.setDouble(4, stock.f_high);
            preparedStatement.setDouble(5, stock.f_low);
            preparedStatement.setDouble(6, stock.f_volume);
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

    // Delete a specific stock data
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

    // Calculate the future value of a stock
    // Use the formula for compound interest: FV = PV * (1 + r)^t
    // Where:
    // FV = Future Value
    // PV = Present Value
    // r = rate of interest
    // t = time in years
    // For example, if the present value of a stock is $1000, the rate of interest is 10% and the time is 5 years,
    // the future value of the stock would be:
    // FV = 1000 * (1 + 0.1)^5 = 1000 * 1.61 = 1610
//     @GetMapping("/future/{symbol}/{time}")
//     @ResponseBody
//     public ArrayList<Double> getFutureValue(@PathVariable String symbol,
//                                             @PathVariable double time) {
//         ArrayList<Double> futureValues = new ArrayList<>();
//         StringBuilder sqlQuery = new StringBuilder("SELECT close FROM stock_data WHERE symbol = ?");
//         // StringBuilder sqlQuery = new StringBuilder("SELECT close, low, high, open FROM stock_data WHERE symbol = ? LIMIT ?");
//         String sqlQueryCOV = "SELECT cov FROM stocks WHERE symbol = ?";

//         try (PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlQuery.toString());
//             PreparedStatement preparedStatementCOV = DBHandler.getInstance().getConnection().prepareStatement(sqlQueryCOV)) {

//             preparedStatement.setString(1, symbol);
//             // preparedStatement.setInt(2, (int) time);
//             preparedStatementCOV.setString(1, symbol);

//             try (ResultSet rs = preparedStatement.executeQuery();
//                 ResultSet rsCOV = preparedStatementCOV.executeQuery()) {

//                 if (rsCOV.next()) {
//                     double cov = rsCOV.getDouble("cov");

//                     while (rs.next()) {
//                         double averagePrice = rs.getDouble("close");
//                         double futureValue = averagePrice * Math.pow(1 + cov, time);
//                         futureValues.add(futureValue);
//                     }
//                 }
//             }
//         } catch (Exception e) {
//             e.printStackTrace();
//             // Return an empty list instead of null to avoid potential NullPointerException
//             return new ArrayList<>();
//         }

//         return futureValues;
//     }
// }


    @GetMapping("/future/{symbol}/{time}")
    @ResponseBody
    public ArrayList<Double> getFutureValue(@PathVariable String symbol,
                                            @PathVariable double time) {

        ArrayList<Double> futureValues = new ArrayList<>();
        StringBuilder sqlQuery = new StringBuilder("SELECT open, close FROM stock_data WHERE symbol = ?");
        String sqlQueryCOV = "SELECT cov FROM stocks WHERE symbol = ?";
        try (PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlQuery.toString());
            PreparedStatement preparedStatementCOV = DBHandler.getInstance().getConnection().prepareStatement(sqlQueryCOV)) {

            preparedStatement.setString(1, symbol);
            preparedStatementCOV.setString(1, symbol);

            try (ResultSet rs = preparedStatement.executeQuery();
                ResultSet rsCOV = preparedStatementCOV.executeQuery()) {

                if (rsCOV.next()) {
                    double cov = rsCOV.getDouble("cov");
                    double randomValue = 0;
                    double futureValue = 0;
                    double close = 0;
                    double average = 0;
                    double open = 0;
                    ArrayList<Integer> randomList = new ArrayList<>();

                    while (rs.next()) {
                        open = rs.getDouble("open");
                        close = rs.getDouble("close");
                        average = (close - open) / 2;
                        if (average < 0) {
                            randomList.add(0);
                        }
                        else if (average == 0) {
                            randomList.add(2);
                        }
                        else {
                            randomList.add(1);
                        }

                        randomValue = randomList.get((int) (Math.random() * randomList.size()));
                        if (randomValue == 0) {
                            futureValue = close - (average * cov * time);
                        }
                        else if (randomValue == 2) {
                            futureValue = close;
                        }
                        else {
                            futureValue = close + (average * cov * time);
                        }
                        futureValues.add(futureValue);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Return an empty list instead of null to avoid potential NullPointerException
            return new ArrayList<>();
        }
        return futureValues;
    }
}
