package backend.src;
import backend.src.User;

public class Main {
    public static void main(String[] args) {
        User user = new User("john_doe", "password123", "john.doe@email.com");
        
        System.out.println("Username: " + user.getUsername());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Password matches: " + user.checkPassword("password123"));
    }
}
