package stocks_api.stocks_api.routes;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import stocks_api.stocks_api.logic.src.DBHandler;
import stocks_api.stocks_api.logic.src.Friendship;
import stocks_api.stocks_api.logic.src.ParserUtil;
import stocks_api.stocks_api.logic.src.Portfolio;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/friends", produces="application/json")

public class FriendshipController {

    private static Connection conn = DBHandler.getInstance().getConnection();

    @PostMapping("/{username}/{target}")
    public void sendFriendRequest(@PathVariable String username, @PathVariable String target) {
        Friendship.sendFriendRequest(username, target, conn);
        //System.out.println(Friendship.checkRequestable(username, target, conn));
    }
    
    @PostMapping("/accept/{username}/{target}")
    public void acceptFriendRequest(@PathVariable String username, @PathVariable String target) {
        Friendship.acceptFriendRequest(username, target, conn);
    }

    @PatchMapping("/reject/{username}/{target}")
    public void rejectFriendRequest(@PathVariable String username, @PathVariable String target) {
        Friendship.rejectFriendRequest(username, target, conn);
    }

    @GetMapping("/viewFriends/{username}")
    public ArrayList<Friendship> viewFriends(@PathVariable String username) {
        try {
            ResultSet rs = Friendship.viewFriends(username, conn);
            ArrayList<Friendship> friends = new ArrayList<Friendship>();
            while (rs.next()){
                Friendship friend = new Friendship(rs.getString("target"), username, "Accepted", null);
                friends.add(friend);
            }
            return friends; 
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/viewSent/{username}")
    public ArrayList<Friendship> viewSentFriendRequests(@PathVariable String username) {
        try {
            ResultSet rs = Friendship.viewSentFriendRequests(username, conn);
            ArrayList<Friendship> friends = new ArrayList<Friendship>();
            while (rs.next()){
                Friendship friend = new Friendship(rs.getString("target"), username, "Pending", null);
                friends.add(friend);
            }
            return friends; 
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

    @GetMapping("/viewIncoming/{username}")
    public ArrayList<Friendship> viewIncomingFriendRequests(@PathVariable String username) {
        try {
            ResultSet rs = Friendship.viewIncomingFriendRequests(username, conn);
            ArrayList<Friendship> friends = new ArrayList<Friendship>();
            while (rs.next()){
                Friendship friend = new Friendship(rs.getString("username"), username, "Pending", null);
                friends.add(friend);
            }
            return friends; 
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @DeleteMapping("/delete/{username}/{target}")
    public void deleteFriendship(@PathVariable String username, @PathVariable String target) {
        Friendship.deleteFriend(username, target, conn);
    }
}