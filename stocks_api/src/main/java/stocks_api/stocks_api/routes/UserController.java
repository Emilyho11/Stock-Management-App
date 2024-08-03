package stocks_api.stocks_api.routes;
import java.sql.Connection;
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

import stocks_api.stocks_api.logic.src.User;
import stocks_api.stocks_api.logic.src.StockList;
import stocks_api.stocks_api.logic.src.DBHandler;
import stocks_api.stocks_api.logic.src.ParserUtil;
import stocks_api.stocks_api.utils.BasicResponse;

@RestController
@RequestMapping(value = "/users", produces="application/json")

public class UserController {

    @GetMapping("/")
    @ResponseBody
    public BasicResponse getUsers() {
        try {
            ResultSet rs = DBHandler.getInstance().executeQuery("SELECT username, email FROM users;");
            String result = ParserUtil.resultSetToJson(rs);
            System.out.println("ResultSet");
            System.out.println(result);
            return BasicResponse.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BasicResponse.ok("FAILED to get users");
    }

    @GetMapping("/{username}")
    @ResponseBody
    public BasicResponse getUser(@PathVariable String username) {
        try {
            String sqlQuery = "SELECT username, email FROM users WHERE username = ?";
            PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlQuery);
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            String result = ParserUtil.resultSetToJson(rs);
            System.out.println("ResultSet");
            System.out.println(result);
            return BasicResponse.ok(result);            
            
        } catch (Exception e) {
            // output error message
            return BasicResponse.ok("FAILED to get user with username: " + username);
        }
    }

    @PostMapping("/")
    @ResponseBody
    public BasicResponse createUser(@RequestBody User user) {
        // Check if user already exists
        if (user.userExists(user.getUsername())) {
            return BasicResponse.ok("User already exists");
            // return "'{'msg': 'User already exists'}'";
        }
        try {
			user = (User)User.create(User::new, user.getUsername(), user.getPassword(), user.getEmail());
            // Check if user was created
            if (!user.userExists(user.getUsername())) {
                return BasicResponse.ok("FAILED to create user with username: " + user.getUsername());
                // return "'{'msg': 'FAILED to create user with username: " + user.getUsername() + "'}'";
            }

            return BasicResponse.ok("User created successfully");
            // return "{'msg': 'User created successfully'}";

        } catch (Exception e) {
            e.printStackTrace();
            // output error message
            return BasicResponse.ok("FAILED to create user with username: " + user.getUsername());
        }
    }

    @PatchMapping("/updateUsername/{oldUsername}/{newUsername}")
    @ResponseBody
    public BasicResponse updateUsername(@PathVariable String oldUsername, @PathVariable String newUsername) {
        try {
            String sqlUpdate = "UPDATE users SET username = ? WHERE username = ?";
            PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlUpdate);
            preparedStatement.setString(1, newUsername);
            preparedStatement.setString(2, oldUsername);
            preparedStatement.executeUpdate();
            return BasicResponse.ok("Username updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            // output error message
            return BasicResponse.ok("FAILED to update username");
        }
    }

    @PatchMapping("/updateEmail/{username}/{email}")
    @ResponseBody
    public BasicResponse updateEmail(@PathVariable String username, @PathVariable String email) {
        try {
            String sqlUpdate = "UPDATE users SET email = ? WHERE username = ?";
            PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlUpdate);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
            return BasicResponse.ok("Email updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            // output error message
            return BasicResponse.ok("FAILED to update email");
        }
    }

    @PatchMapping("/updatePassword/{username}/{password}")
    @ResponseBody
    public BasicResponse updatePassword(@PathVariable String username, @PathVariable String password) {
        try {
            String sqlUpdate = "UPDATE users SET password = ? WHERE username = ?";
            PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement(sqlUpdate);
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
            return BasicResponse.ok("Password updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            // output error message
            return BasicResponse.ok("FAILED to update password");
        }
    }

    @DeleteMapping("/")
    @ResponseBody
    public BasicResponse deleteUser(@RequestBody User user) {
        // Check if user is in database
        if (!user.userExists(user.getUsername())) {
            return BasicResponse.ok("User does not exist");
        }
        try {
			user.delete();
            // Check if user was deleted
            if (user.userExists(user.getUsername())) {
                return BasicResponse.ok("FAILED to delete user");
            }
            return BasicResponse.ok("User deleted successfully");

        } catch (Exception e) {
            e.printStackTrace();
            // output error message
            return BasicResponse.ok("FAILED to delete user");
        }
    }
    
    // Api that checks if user exists in database
    @GetMapping("/exists/{username}")
    @ResponseBody
    public BasicResponse userExists(@PathVariable String username) {
        try {
            if (User.userExists(username)) {
                return BasicResponse.ok("User exists");
            }
            return BasicResponse.ok("User does not exist");
        } catch (Exception e) {
            e.printStackTrace();
            // output error message
            return BasicResponse.ok("FAILED to check if user exists");
        }
    }

    @PostMapping("/login")
    @ResponseBody
    public BasicResponse login(@RequestBody User user) {
        if (!user.userExists(user.getUsername())) {
            return BasicResponse.ok("User does not exist");
        }

        try {
            if (user.checkPassword(user.getPassword())) {
                return BasicResponse.value("ok", user.getUsername());
            }
            return BasicResponse.ok("Login failed");
        } catch (Exception e) {
            e.printStackTrace();
            // output error message
            return BasicResponse.ok("FAILED to login");
        }
    }
}