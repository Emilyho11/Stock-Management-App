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
import stocks_api.stocks_api.logic.src.Friendship;
import stocks_api.stocks_api.logic.src.ParserUtil;


@RestController
@RequestMapping(value = "/friends", produces="application/json")

public class FriendshipController {

    private static Connection conn = DBHandler.getInstance().getConnection();

    @PostMapping("/{username}/{target}")
    public void sendFriendRequest(@PathVariable String username, @PathVariable String target) {
        Friendship.sendFriendRequest(username, target, conn);
    }
    
    @PatchMapping("/accept/{username}/{target}")
    public void acceptFriendRequest(@PathVariable String username, @PathVariable String target) {
        Friendship.acceptFriendRequest(username, target, conn);
    }

    @PatchMapping("/reject/{username}/{target}")
    public void rejectFriendRequest(@PathVariable String username, @PathVariable String target) {
        Friendship.rejectFriendRequest(username, target, conn);
    }

    @GetMapping("/viewFriends/{username}")
    public String viewFriends(@PathVariable String username) {
        ResultSet rs = Friendship.viewFriends(username, conn);
        String result = ParserUtil.resultSetToJson(rs);
        return "'{'content': " + result + "}'";
    }

    @GetMapping("/viewSent/{username}")
    public String viewSentFriendRequests(@PathVariable String username) {
        ResultSet rs = Friendship.viewSentFriendRequests(username, conn);
        String result = ParserUtil.resultSetToJson(rs);
        return "'{'content': " + result + "}'";
    }

    @GetMapping("/viewIncoming/{username}")
    public String viewIncomingFriendRequests(@PathVariable String username) {
        ResultSet rs = Friendship.viewIncomingFriendRequests(username, conn);
        String result = ParserUtil.resultSetToJson(rs);
        return "'{'content': " + result + "}'";
    }

    @DeleteMapping("/delete/{username}/{target}")
    public void deleteFriendship(@PathVariable String username, @PathVariable String target) {
        Friendship.deleteFriend(username, target, conn);
    }
}