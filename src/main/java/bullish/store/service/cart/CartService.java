package bullish.store.service.cart;

import bullish.store.communication.cart.CartAddProductRequest;
import bullish.store.entity.CartEntity;

public interface CartService {

    void addProductToCart(CartAddProductRequest request);
    void removeProductFromCart(Long productId);
    CartEntity getCart();

}
