package bullish.store.service.cart;

import bullish.store.communication.cart.CartAddProductRequest;
import bullish.store.entity.CartEntity;

import java.util.List;

public interface CartService {

    void addProduct(CartAddProductRequest request);
    void removeProduct(Long productId);
    CartEntity getCart();

}
