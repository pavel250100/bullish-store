package bullish.store;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Store REST API Documentation",
                version = "v1.0"
        )
)
public class StoreApplication {
    public static void main(String... args) {
        SpringApplication.run(StoreApplication.class, args);
    }
}

