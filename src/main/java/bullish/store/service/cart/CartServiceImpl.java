package bullish.store.service.cart;

import bullish.store.communication.cart.CartAddProductRequest;
import bullish.store.entity.CartEntity;
import bullish.store.entity.CartItemEntity;
import bullish.store.entity.ProductEntity;
import bullish.store.exception.cart.CartItemNotFoundException;
import bullish.store.exception.cart.CartNotEnoughStockException;
import bullish.store.exception.cart.CartNotFoundException;
import bullish.store.exception.product.ProductConflictException;
import bullish.store.exception.product.ProductNotFoundException;
import bullish.store.repository.CartRepository;
import bullish.store.repository.ProductRepository;
import bullish.store.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    public final ProductRepository productRepository;
    public final CartRepository cartRepository;

    @Transactional
    @Override
    public void addProduct(CartAddProductRequest request) {
        String username = AuthUtil.extractUsernameFromContext();

        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(request.getProductId()));

        Long availableQuantity = product.getStock().getQuantity();

        if (availableQuantity < request.getQuantity()) {
            throw new CartNotEnoughStockException(request.getProductId());
        }

        CartEntity cart = cartRepository.findByUsername(username)
                .orElseThrow(() -> new CartNotFoundException(username));

        Optional<CartItemEntity> existingCartItem = cart.getItems().stream()
                .filter(item -> Objects.equals(item.getProduct().getId(), request.getProductId()))
                .findFirst();

        if (existingCartItem.isPresent()) {
            CartItemEntity cartItem = existingCartItem.get();
            if (!cartItem.getPriceWhenAdded().equals(product.getPrice())) {
                cartItem.setPriceWhenAdded(product.getPrice());
                throw new ProductConflictException(product);
            }
            Long previouslyRequestedQuantity = cartItem.getQuantity();
            Long totalRequestedQuantity = previouslyRequestedQuantity + request.getQuantity();
            if (availableQuantity < totalRequestedQuantity) {
                throw new CartNotEnoughStockException(request.getProductId());
            }
            cartItem.setQuantity(totalRequestedQuantity);
            return;
        }

        CartItemEntity itemToAdd = CartItemEntity.builder()
                .product(product)
                .quantity(request.getQuantity())
                .priceWhenAdded(product.getPrice())
                .build();

        cart.addItem(itemToAdd);
    }

    @Transactional
    @Override
    public void removeProduct(Long productId) {
        String username = AuthUtil.extractUsernameFromContext();

        CartEntity cart = cartRepository.findByUsername(username)
                .orElseThrow(() -> new CartNotFoundException(username));

        CartItemEntity itemToRemove = cart
                .getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new CartItemNotFoundException(productId, username));

        cart.removeItem(itemToRemove);
    }

    @Override
    @Transactional(readOnly = true)
    public CartEntity getCart() {
        String username = AuthUtil.extractUsernameFromContext();

        CartEntity cart = cartRepository.findByUsername(username)
                .orElseThrow(() -> new CartNotFoundException(username));

        for (CartItemEntity item : cart.getItems()) {
            BigDecimal latestPrice = item.getProduct().getPrice();
            item.setPriceWhenAdded(latestPrice);
        }

        return cart;
    }


}
