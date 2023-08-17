package bullish.store.service.cart;

import bullish.store.communication.cart.CartAddProductRequest;
import bullish.store.entity.CartEntity;
import bullish.store.entity.CartItemEntity;
import bullish.store.entity.ProductEntity;
import bullish.store.entity.StockEntity;
import bullish.store.repository.CartRepository;
import bullish.store.repository.ProductRepository;
import bullish.store.util.AuthUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    public void ShouldAddProductToCart() {
        CartAddProductRequest request = CartAddProductRequest.builder()
                .productId(1L)
                .quantity(1L)
                .build();

        ProductEntity product = new ProductEntity();
        StockEntity stock = new StockEntity();
        stock.setQuantity(1L);
        product.setStock(stock);
        String username = "testUser";

        CartEntity cart = CartEntity.builder().build();

        when(productRepository.findById(request.getProductId())).thenReturn(Optional.of(product));
        when(cartRepository.findByUsername(username)).thenReturn(Optional.of(cart));

        try (MockedStatic<AuthUtil> mockedAuthUtil = Mockito.mockStatic(AuthUtil.class)) {
            mockedAuthUtil.when(AuthUtil::extractUsernameFromContext).thenReturn(username);
            cartService.addProductToCart(request);
            assertEquals(cart.getItems().size(), 1);
        }
    }

    @Test
    public void ShouldRemoveProductFromCart() {
        Long productIdToRemove = 1L;
        String username = "user";
        CartEntity cart = new CartEntity();
        ProductEntity product = new ProductEntity();
        product.setId(productIdToRemove);
        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setProduct(product);
        cart.setItems(new ArrayList<>(List.of(cartItem)));

        when(cartRepository.findByUsername(username)).thenReturn(Optional.of(cart));

        try (MockedStatic<AuthUtil> mockedAuthUtil = Mockito.mockStatic(AuthUtil.class)) {
            mockedAuthUtil.when(AuthUtil::extractUsernameFromContext).thenReturn(username);
            cartService.removeProductFromCart(productIdToRemove);
            assertEquals(cart.getItems().size(), 0);
        }
    }

}