package stocks_api.stocks_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

import stocks_api.stocks_api.logic.Main;

@SpringBootApplication
public class StocksApiApplication {

	public static void main(String[] args) {
		// Initialize the logic main handler
		Main.Initialize();

		SpringApplication.run(StocksApiApplication.class, args);
	}


}
