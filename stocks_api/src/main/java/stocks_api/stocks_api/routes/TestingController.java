package stocks_api.stocks_api.routes;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping(value = "/", produces="application/json")
public class TestingController {
    
    public TestingController() {
        System.out.println("TestingController created");
    }

    @GetMapping(value="/test")
    public String test() {
        System.out.println("Test endpoint hit");
        return "{\"message\": \"Hello, World!\"}";
    }
}
