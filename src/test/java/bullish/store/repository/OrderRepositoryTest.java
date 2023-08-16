package bullish.store.repository;

import bullish.store.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;


    @Test
    void ShouldReturnOrdersByUsername() {
        String username = "user";
        UserEntity user = new UserEntity();
        user.setUsername(username);
        UserDetailsEntity userDetails = new UserDetailsEntity();
        userDetails.setUser(user);
        user.setDetails(userDetails);

        entityManager.persistAndFlush(user);

        ProductEntity product = new ProductEntity();

        entityManager.persistAndFlush(product);

        OrderEntity order = new OrderEntity();
        order.setUser(user);

        OrderItemEntity orderItem = new OrderItemEntity();
        orderItem.setOrder(order);
        orderItem.setProduct(product); // Set the product
        orderItem.setQuantity(10L);

        order.setItems(List.of(orderItem)); // Add the orderItem to the order

        entityManager.persistAndFlush(order);

        List<OrderEntity> found = orderRepository.findByUsername(username);
        assertFalse(found.isEmpty());
        assertEquals(found.size(), 1);
    }


}