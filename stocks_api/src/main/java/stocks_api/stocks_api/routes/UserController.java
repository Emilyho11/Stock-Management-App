package stocks_api.stocks_api.routes;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import stocks_api.stocks_api.logic.src.User;
import stocks_api.stocks_api.logic.src.DBHandler;
import java.sql.ResultSet;
import stocks_api.stocks_api.logic.src.ParserUtil;

@RestController
@RequestMapping(value = "/users", produces="application/json")

public class UserController {

    @GetMapping("/")
    public String getUsers() {
        // return User.getAll();
        try {
            ResultSet rs = DBHandler.getInstance().executeQuery("SELECT * FROM users;");// + User.TABLE_NAME);
            String result = ParserUtil.resultSetToJson(rs);
            System.out.println("ResultSet");
            System.out.println(result);
            return "'{'content': " + result + "}'";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "'{'msg': 'FAILED'}'";
    }
    

    // @GetMapping("/users/{id}")
    // public User getUser(@PathVariable String id) {
    //     return "HELLO";
    // }

    // @PostMapping("/users")
    // public User createUser(@RequestBody User user) {
    //     // return User.create(user);
    // }

    // @PutMapping("/users/{id}")
    // public User updateUser(@PathVariable String id, @RequestBody User user) {
    //     // return User.update(id, user);
    // }

    // @DeleteMapping("/users/{id}")
    // public void deleteUser(@PathVariable String id) {
    //     // User.delete(id);
    // }
}