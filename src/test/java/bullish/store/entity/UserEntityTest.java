package bullish.store.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void UserEntityCreationTest() {
        UserRoleEntity userRole = UserRoleEntity.builder()
                .role(UserRoleEntity.Role.ADMIN)
                .build();

        entityManager.persistAndFlush(userRole);

        UserDetailsEntity userDetails = UserDetailsEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@gmail.com")
                .build();

        UserEntity user = UserEntity.builder()
                .details(userDetails)
                .roles(Set.of(userRole))
                .build();

        entityManager.persistAndFlush(user);

        UserEntity retrievedUser = entityManager.find(UserEntity.class, user.getId());

        assertEquals(retrievedUser.getUsername(), user.getUsername());
        assertEquals(retrievedUser.getPassword(), user.getPassword());
        assertThat(retrievedUser.getRoles()).contains(userRole);
        assertEquals(retrievedUser.getDetails().getEmail(), userDetails.getEmail());
        assertEquals(retrievedUser.getDetails().getFirstName(), userDetails.getFirstName());
        assertEquals(retrievedUser.getDetails().getLastName(), userDetails.getLastName());
    }

    @Test
    public void ShouldDeleteDetails_WhenUserIsDeleted() {
        UserRoleEntity userRole = UserRoleEntity.builder()
                .role(UserRoleEntity.Role.ADMIN)
                .build();

        UserDetailsEntity userDetails = UserDetailsEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@gmail.com")
                .build();

        UserEntity user = UserEntity.builder()
                .details(userDetails)
                .roles(Set.of(userRole))
                .build();

        entityManager.persistAndFlush(user);

        entityManager.remove(user);
        entityManager.flush();

        UserEntity retrievedUser = entityManager.find(UserEntity.class, user.getId());
        UserDetailsEntity retrievedDetails = entityManager.find(UserDetailsEntity.class, userDetails.getUserId());

        assertNull(retrievedUser);
        assertNull(retrievedDetails);
    }

    @Test
    public void ShouldInitializeUser_AndAnEmptyCart() {
        UserRoleEntity userRole = UserRoleEntity.builder()
                .role(UserRoleEntity.Role.ADMIN)
                .build();

        UserDetailsEntity userDetails = UserDetailsEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@gmail.com")
                .build();

        UserEntity user = UserEntity.builder()
                .details(userDetails)
                .roles(Set.of(userRole))
                .build();

        UserEntity savedUser = entityManager.persistAndFlush(user);

        CartEntity savedCart = entityManager.find(CartEntity.class, savedUser.getId());

        assertNotNull(savedCart);
        assertEquals(savedCart.getItems().size(), 0);
    }

}