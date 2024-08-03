package stocks_api.stocks_api.routes;
import java.sql.Connection;
import java.sql.ResultSet;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import stocks_api.stocks_api.logic.src.DBHandler;
import stocks_api.stocks_api.logic.src.ParserUtil;
import stocks_api.stocks_api.logic.src.Portfolio;


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
    public String getUserPortfolios(@PathVariable String username) {
        ResultSet rs = Portfolio.findByOwner(username, conn);
        String result = ParserUtil.resultSetToJson(rs);
        return "'{'content': " + result + "}'";
    }

    @GetMapping("/get/{username}/{name}")
    public String getUserPortfolioByName(@PathVariable String username, @PathVariable String name) {
        ResultSet rs = Portfolio.findByOwnerAndName(username, name, conn);
        String result = ParserUtil.resultSetToJson(rs);
        return "'{'content': " + result + "}'";
    }

    @GetMapping("/getBalance/{id}")
    public String getBalance(@PathVariable int id) {
        Double bal = Portfolio.getBalance(id, conn);
        return "'{'content': " + bal + "}'";
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
}