package stocks_api.stocks_api.routes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;


import stocks_api.stocks_api.logic.src.DBHandler;
import stocks_api.stocks_api.logic.src.ParserUtil;
import stocks_api.stocks_api.logic.src.Portfolio;
import stocks_api.stocks_api.logic.src.Reviews;
import stocks_api.stocks_api.logic.src.Stocks;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/portfolio", produces="application/json")

public class PortfolioController {

    private static Connection conn = DBHandler.getInstance().getConnection();

    @PostMapping("/{username}/{name}")
    public void createPortfolio(@PathVariable String username, @PathVariable String name) {
        Portfolio.createPortfolio(username, name, conn);
    }
    
    @PatchMapping("/rename/{id}/{newname}")
    public void renamePortfolio(@PathVariable int id, @PathVariable String newname) {
        Portfolio.renamePortfolio(id, newname, conn);
    }

    @GetMapping("/get/{username}")
    public ArrayList<Portfolio> getUserPortfolios(@PathVariable String username) {
        try {
            ResultSet rs = Portfolio.findByOwner(username, conn);
            ArrayList<Portfolio> portfolios = new ArrayList<Portfolio>();
            while (rs.next()){
                Portfolio portfolio = new Portfolio(rs.getInt("portfolio_id"), rs.getString("name"), rs.getDouble("beta"));
                portfolios.add(portfolio);
            }
            return portfolios;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/get/{username}/{name}")
    public String getUserPortfolioByName(@PathVariable String username, @PathVariable String name) {
        ResultSet rs = Portfolio.findByOwnerAndName(username, name, conn);
        String result = ParserUtil.resultSetToJson(rs);
        return "'{'content': " + result + "}'";
    }

    @GetMapping("/getStocks/{id}")
    public ArrayList<Object> getUserPortfolioStocks(@PathVariable int id) {
        try {
            ResultSet rs = Portfolio.getStocks(id, conn);
            ArrayList<Object> stocks = new ArrayList<Object>();
            while (rs.next()){
                ArrayList<Object> stock = new ArrayList<Object>();
                stock.add(rs.getString("symbol"));
                stock.add(rs.getInt("total"));
                stocks.add(stock);
            }
            return stocks; 
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/getBalance/{id}")
    public double getBalance(@PathVariable int id) {
        double bal = Portfolio.getBalance(id, conn);
        return bal;
    }

    @GetMapping("/getCashHistory/{id}")
    public String getCashHistory(@PathVariable int id) {
        ResultSet rs = Portfolio.getPortfolioCashHistory(id, conn);
        String result = ParserUtil.resultSetToJson(rs);
        return "'{'content': " + result + "}'";
    }

    @PostMapping("/withdraw/{id}/{amount}")
    public void withdrawCash(@PathVariable int id, @PathVariable double amount) {
        Portfolio.withdrawCash(id, amount, conn);
    }

    @PostMapping("/deposit/{id}/{amount}")
    public void depositCash(@PathVariable int id, @PathVariable double amount) {
        Portfolio.depositCash(id, amount, conn);
    }

    @GetMapping("/check/{id}/{symbol}")
    public int getIfBought(@PathVariable int id, @PathVariable String symbol) {
        return Portfolio.getTotalStock(id, symbol, conn);
    }

    @PostMapping("/trade/{id}/{symbol}/{quantity}/{type}")
    public void tradeStocksCreate(@PathVariable int id, @PathVariable String symbol, @PathVariable int quantity, @PathVariable String type) {
        Portfolio.tradeStocksCreate(id, symbol, type, quantity, conn);
    }

    @PatchMapping("/trade/{id}/{symbol}/{amount}/{type}")
    public void tradeStocksUpdate(@PathVariable int id, @PathVariable String symbol, @PathVariable int amount, @PathVariable String type) {
        Portfolio.tradeStocksUpdate(id, symbol, type, amount, conn);
    }

    @GetMapping("/estimateStock/{id}")
    public String estimateStockValue(@PathVariable int id) {
        double result = Portfolio.estimateStockValue(id, conn);
        return "'{'content': " + result + "}'";
    }

    @GetMapping("/estimateValue/{id}")
    public double estimateValue(@PathVariable int id) {
        return Portfolio.estimatePortfolioValue(id, conn);
    }

    @GetMapping("/getTransactions/{id}")
    public String getPortfolioTransactions(@PathVariable int id) {
        ResultSet rs = Portfolio.getPortfolioTransactions(id, conn);
        String result = ParserUtil.resultSetToJson(rs);
        return "'{'content': " + result + "}'";
    }

    @DeleteMapping("/delete/{id}")
    public void deletePortfolio(@PathVariable int id) {
        Portfolio.deletePortfolio(id, conn);
    }

    // Create an api for the above function calculatePortfolioCOV
    @GetMapping("/calculatePortfolioCOV/{id}")
    public Map<String, Double> calculatePortfolioCOV(@PathVariable int id) {
        Map<String, Double> covMap = Stocks.calculatePortfolioCOV(id);
        return covMap;
    }

    // Calculate correlation
    @GetMapping("/calculateCorrelation/{symbol1}/{symbol2}")
    public double calculateCorrelation(@PathVariable String symbol1, @PathVariable String symbol2) {
        double correlation = Stocks.calculateCorrelation(symbol1, symbol2);
        return correlation;
    }

    // Calculate covariance between two stocks api
    @GetMapping("/calculateCovBetween2/{symbol1}/{symbol2}")
    public double calculateCovBetween2(@PathVariable String symbol1, @PathVariable String symbol2) {
        double covariance = Stocks.calculateCOVBetweenTwoStocks(symbol1, symbol2);
        return covariance;
    }
}