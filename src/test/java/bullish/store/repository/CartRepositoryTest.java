package bullish.store.repository;

import bullish.store.entity.CartEntity;
import bullish.store.entity.UserDetailsEntity;
import bullish.store.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class CartRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CartRepository cartRepository;

    @Test
    public void ShouldReturnCartByUsername() {
        UserEntity user = new UserEntity();
        user.setUsername("user");
        UserDetailsEntity userDetails = new UserDetailsEntity();
        userDetails.setUser(user);
        user.setDetails(userDetails);
        entityManager.persistAndFlush(user);

        Optional<CartEntity> found = cartRepository.findByUsername("user");
        assertTrue(found.isPresent());
        assertEquals(found.get(), user.getCart());
    }
}