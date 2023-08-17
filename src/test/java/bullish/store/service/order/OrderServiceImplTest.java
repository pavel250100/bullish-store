package bullish.store.service.order;

import bullish.store.entity.*;
import bullish.store.exception.order.OrderConflictException;
import bullish.store.repository.CartRepository;
import bullish.store.repository.UserRepository;
import bullish.store.util.AuthUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private static final String username = "test-user";

    private UserEntity dummyUser() {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        return user;
    }

    private CartEntity dummyCart() {
        CartEntity cart = new CartEntity();

        StockEntity stock1 = new StockEntity();
        stock1.setQuantity(10L);
        ProductEntity product1 = new ProductEntity();
        product1.setPrice(BigDecimal.TEN);
        product1.setStock(stock1);
        CartItemEntity cartItem1 = CartItemEntity.builder()
                .product(product1)
                .quantity(10L)
                .priceWhenAdded(BigDecimal.TEN)
                .build();

        StockEntity stock2 = new StockEntity();
        stock2.setQuantity(10L);
        ProductEntity product2 = new ProductEntity();
        product2.setPrice(BigDecimal.ONE);
        product2.setStock(stock2);
        CartItemEntity cartItem2 = CartItemEntity.builder()
                .product(product2)
                .quantity(10L)
                .priceWhenAdded(BigDecimal.ONE)
                .build();

        cart.setItems(new ArrayList<>(List.of(cartItem1, cartItem2)));
        return cart;
    }

    @Test
    public void ShouldPlaceOrderSuccessfully() {
        CartEntity cart = dummyCart();
        UserEntity user = dummyUser();
        when(cartRepository.findByUsername(username)).thenReturn(Optional.of(cart));
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        try (MockedStatic<AuthUtil> mockedAuthUtil = Mockito.mockStatic(AuthUtil.class)) {
            mockedAuthUtil.when(AuthUtil::extractUsernameFromContext).thenReturn(username);
            OrderEntity order = orderService.placeOrder();
            assertEquals(order.getTotalPrice(), new BigDecimal(110).setScale(5, RoundingMode.HALF_UP));
            assertEquals(cart.getItems().size(), 0);
            assertEquals(user.getOrders().size(), 1);
            for (CartItemEntity cartItem: cart.getItems()) {
                StockEntity stock = cartItem.getProduct().getStock();
                assertEquals(stock.getQuantity(), 0);
            }
        }
    }

    @Test
    public void ShouldThrowOrderConflictException_WhenProductOutOfStock() {
        CartEntity cart = dummyCart();

        StockEntity stock = new StockEntity();
        stock.setQuantity(5L); // new stock
        ProductEntity product = new ProductEntity();
        product.setPrice(BigDecimal.ONE);
        product.setStock(stock);
        CartItemEntity cartItem = CartItemEntity.builder()
                .product(product)
                .quantity(10L)
                .priceWhenAdded(BigDecimal.ONE)
                .build();

        cart.addItem(cartItem);

        when(cartRepository.findByUsername(username)).thenReturn(Optional.of(cart));

        try (MockedStatic<AuthUtil> mockedAuthUtil = Mockito.mockStatic(AuthUtil.class)) {
            mockedAuthUtil.when(AuthUtil::extractUsernameFromContext).thenReturn(username);
            assertThrows(OrderConflictException.class, () -> orderService.placeOrder());
        }
    }

    @Test
    public void ShouldThrowOrderConflictException_WhenPriceForProductHasChanged() {
        CartEntity cart = dummyCart();

        StockEntity stock = new StockEntity();
        stock.setQuantity(10L);
        ProductEntity product = new ProductEntity();
        product.setPrice(BigDecimal.TEN); // new price
        product.setStock(stock);
        CartItemEntity cartItem = CartItemEntity.builder()
                .product(product)
                .quantity(10L)
                .priceWhenAdded(BigDecimal.ONE)
                .build();

        cart.addItem(cartItem);

        when(cartRepository.findByUsername(username)).thenReturn(Optional.of(cart));

        try (MockedStatic<AuthUtil> mockedAuthUtil = Mockito.mockStatic(AuthUtil.class)) {
            mockedAuthUtil.when(AuthUtil::extractUsernameFromContext).thenReturn(username);
            assertThrows(OrderConflictException.class, () -> orderService.placeOrder());
        }
    }

}