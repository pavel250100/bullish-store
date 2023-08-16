package bullish.store.configuration;

import bullish.store.communication.product.Product;
import bullish.store.communication.stock.Stock;
import bullish.store.entity.*;
import bullish.store.entity.type.DealType;
import bullish.store.repository.ProductRepository;
import bullish.store.repository.StockRepository;
import bullish.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;


@Configuration
@RequiredArgsConstructor
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
    public final UserRepository userRepository;
    public final ProductRepository productRepository;
    public final StockRepository stockRepository;
    public final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase() {
        UserEntity adminUser = user("admin", "admin", UserRoleEntity.Role.ADMIN);
        UserEntity customer = user("user", "user", UserRoleEntity.Role.USER);
        ProductEntity product1 = product("Bread", BigDecimal.TEN, 10L, null);

        DealEntity deal2 = new DealEntity();
        deal2.setDealType(DealType.BUY_N_GET_DISCOUNT);
        deal2.setDiscountPercentage(BigDecimal.valueOf(20));
        deal2.setBuyQuantity(4L);
        ProductEntity product2 = product("Computer", BigDecimal.TEN, 20L, deal2);
        ProductEntity product3 = product("Yogurt", new BigDecimal("10.5"), 20L, null);

        List<ProductEntity> products = List.of(product1, product2, product3);

        return args -> {
            log.info("Preloading " + userRepository.save(adminUser));
            log.info("Preloading " + userRepository.save(customer));
            log.info("Preloading " + productRepository.saveAll(products));
        };
    }

    private ProductEntity product(String name, BigDecimal price, Long quantity, DealEntity deal) {
        ProductEntity product = new ProductEntity();
        product.setPrice(price);
        product.setName(name);

        StockEntity stock = new StockEntity();
        stock.setQuantity(quantity);
        product.setStock(stock);

        if (deal != null) {
            deal.setProduct(product);
            product.setDeal(deal);
        };
        return product;
    }

    private UserEntity user(String username, String password, UserRoleEntity.Role role) {
        UserDetailsEntity adminDetails = UserDetailsEntity.builder()
                .firstName("First Name")
                .lastName("Last Name")
                .email("mail@gmail.com")
                .build();

        UserRoleEntity adminRole = UserRoleEntity.builder().role(role).build();

        return UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .details(adminDetails)
                .roles(Set.of(adminRole))
                .build();
    }
}