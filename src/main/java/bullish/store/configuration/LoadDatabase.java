package bullish.store.configuration;

import bullish.store.entity.Product;
import bullish.store.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(ProductRepository repository) {
        return args -> {
//            log.info("Preloading " + repository.save(new Product("Product 1", "Desc 1", BigDecimal.TEN)));
//            log.info("Preloading " + repository.save(new Product("Product 2", "Desc 2", BigDecimal.TEN)));
//            log.info("Preloading " + repository.save(new Product("Product 3", "Desc 3", BigDecimal.TEN)));
        };
    }
}