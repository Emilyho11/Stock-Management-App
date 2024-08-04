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
import org.springframework.web.bind.annotation.CrossOrigin;


import stocks_api.stocks_api.logic.src.DBHandler;
import stocks_api.stocks_api.logic.src.ParserUtil;
import stocks_api.stocks_api.logic.src.Portfolio;
import stocks_api.stocks_api.logic.src.StockList;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/stocklist", produces="application/json")

public class StockListController {

    private static Connection conn = DBHandler.getInstance().getConnection();

    @PostMapping("/{username}/{name}")
    public void createStockList(@PathVariable String username, @PathVariable String name) {
        //User.createStockList(username, name, conn);
    }
    
    @PostMapping("/{username}/{id}/{name}")
    public void createStockListInPortfolio(@PathVariable String username, @PathVariable int id, @PathVariable String name) {
        Portfolio.createStockListInPortfolio(id, name, username, conn);
    }

    // @GetMapping("/get/{username}")
    // public String getUserStockLists(@PathVariable String username) {
    //     ResultSet rs = User.getUserStockLists(username, conn);
    //     String result = ParserUtil.resultSetToJson(rs);
    //     return "'{'content': " + result + "}'";
    // }

    @GetMapping("/get/{id}")
    public String getPortfolioStockLists(@PathVariable int id) {
        ResultSet rs = Portfolio.getStockLists(id, conn);
        String result = ParserUtil.resultSetToJson(rs);
        return "'{'content': " + result + "}'";
    }

    @PatchMapping("/changePrivacy/{id}/{privacy}")
    public void changePrivacy(@PathVariable int id, @PathVariable String privacy) {
        StockList.changePrivacy(id, privacy, conn);
    }

    @PatchMapping("/rename/{id}/{newname}")
    public void renameStockList(@PathVariable int id, @PathVariable String newname) {
        StockList.renameStockList(id, newname, conn);
    }

    @PostMapping("/movein/{portfolio_id}/{stocklist_id}")
    public void moveInStockListPortfolio(@PathVariable int portfolio_id, @PathVariable int stocklist_id) {
        Portfolio.addStockListToPortfolio(portfolio_id, stocklist_id, conn);
    }

    @DeleteMapping("/moveout/{portfolio_id}/{stocklist_id}")
    public void moveOutStockListFromPortfolio(@PathVariable int portfolio_id, @PathVariable int stocklist_id) {
        Portfolio.deleteStockListFromPortfolio(portfolio_id, stocklist_id, conn);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteStockList(@PathVariable int id) {
        StockList.deleteStockListGlobal(id, conn);
    }
}