package bullish.store.configuration;

import bullish.store.entity.UserDetailsEntity;
import bullish.store.entity.UserEntity;
import bullish.store.entity.UserRoleEntity;
import bullish.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;


@Configuration
@RequiredArgsConstructor
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
    public final UserRepository userRepository;
    public final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase() {
        UserDetailsEntity adminDetails = UserDetailsEntity.builder()
                .firstName("Admin First Name")
                .lastName("Admin Last Name")
                .email("admin@gmail.com")
                .build();

        UserRoleEntity adminRole = UserRoleEntity.builder().role(UserRoleEntity.Role.ADMIN).build();

        UserEntity adminUser = UserEntity.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .details(adminDetails)
                .roles(Set.of(adminRole))
                .build();

        UserDetailsEntity userDetails = UserDetailsEntity.builder()
                .firstName("User First Name")
                .lastName("User Last Name")
                .email("user@gmail.com")
                .build();

        UserRoleEntity userRole = UserRoleEntity.builder().role(UserRoleEntity.Role.USER).build();

        UserEntity user = UserEntity.builder()
                .username("user")
                .password(passwordEncoder.encode("user"))
                .details(userDetails)
                .roles(Set.of(userRole))
                .build();

        return args -> {
            log.info("Preloading " + userRepository.save(adminUser));
            log.info("Preloading " + userRepository.save(user));
        };
    }
}