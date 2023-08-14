package bullish.store.service.cart;

import bullish.store.communication.cart.CartAddProductRequest;
import bullish.store.entity.CartEntity;
import bullish.store.entity.CartItemEntity;
import bullish.store.entity.ProductEntity;
import bullish.store.exception.cart.CartItemNotFoundException;
import bullish.store.exception.cart.CartNotEnoughStockException;
import bullish.store.exception.cart.CartNotFoundException;
import bullish.store.exception.product.ProductNotFoundException;
import bullish.store.repository.CartRepository;
import bullish.store.repository.ProductRepository;
import bullish.store.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    public final ProductRepository productRepository;
    public final CartRepository cartRepository;

    @Transactional
    @Override
    public CartEntity addProduct(CartAddProductRequest request) {
        String username = AuthUtil.extractUsernameFromContext();

        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(request.getProductId()));

        Long availableQuantity = product.getStock().getQuantity();

        if (availableQuantity < request.getQuantity()) {
            throw new CartNotEnoughStockException(request.getProductId());
        }

        CartEntity cart = cartRepository.findByUsername(username)
                .orElseThrow(() -> new CartNotFoundException(username));

        CartItemEntity itemToAdd = CartItemEntity.builder()
                .product(product)
                .quantity(request.getQuantity())
                .build();

        cart.addItem(itemToAdd);

        return cart;
    }

    @Transactional
    @Override
    public CartEntity removeProduct(Long productId) {
        String username = AuthUtil.extractUsernameFromContext();

        CartEntity cart = cartRepository.findByUsername(username)
                .orElseThrow(() -> new CartNotFoundException(username));

        CartItemEntity itemToRemove = cart
                .getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new CartItemNotFoundException(productId, username));

        cart.removeItem(itemToRemove);

        return cart;
    }

    @Override
    public CartEntity getCart() {
        String username = AuthUtil.extractUsernameFromContext();

        return cartRepository.findByUsername(username)
                .orElseThrow(() -> new CartNotFoundException(username));
    }


}
