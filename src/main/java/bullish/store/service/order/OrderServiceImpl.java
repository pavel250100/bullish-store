package bullish.store.service.order;

import bullish.store.entity.*;
import bullish.store.exception.cart.CartNotFoundException;
import bullish.store.exception.order.OrderConflictException;
import bullish.store.exception.user.UserNotFoundException;
import bullish.store.repository.CartRepository;
import bullish.store.repository.UserRepository;
import bullish.store.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public OrderEntity placeOrder() {
        String username = AuthUtil.extractUsernameFromContext();

        CartEntity cart = cartRepository.findByUsername(username)
                .orElseThrow(() -> new CartNotFoundException(username));

        List<ProductEntity> outOfStockProducts = getOutOfStockItems(cart);
        List<ProductEntity> priceChangedProducts = getChangedPriceProducts(cart);
        if (!outOfStockProducts.isEmpty() || !priceChangedProducts.isEmpty()) {
            updateCartPricing(cart);
            throw new OrderConflictException(outOfStockProducts, priceChangedProducts);
        }

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        OrderEntity newOrder = new OrderEntity();
        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal totalDiscount = BigDecimal.ZERO;
        for (CartItemEntity cartItem : cart.getItems()) {
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setPriceWhenOrdered(cartItem.getProduct().getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setProduct(cartItem.getProduct());

            BigDecimal itemTotalDiscount = cartItem.totalDiscount();
            BigDecimal itemTotalPrice = cartItem.totalPrice();
            orderItem.setTotalDiscount(itemTotalPrice);
            orderItem.setTotalPrice(itemTotalDiscount);
            orderItem.setDealApplied(cartItem.getAppliedDeal());
            newOrder.addOrderItem(orderItem);

            totalPrice = totalPrice.add(itemTotalPrice);
            totalDiscount = totalDiscount.add(itemTotalDiscount);

            // decrease a stock
            StockEntity existingStock = orderItem.getProduct().getStock();
            existingStock.setQuantity(existingStock.getQuantity() - cartItem.getQuantity());
        }

        newOrder.setTotalPrice(totalPrice);
        newOrder.setTotalDiscount(totalDiscount);

        // clean a cart
        cart.getItems().clear();

        user.addOrder(newOrder);

        return newOrder;
    }

    private void updateCartPricing(CartEntity cart) {
        for (CartItemEntity item : cart.getItems()) {
            item.setPriceWhenAdded(item.getProduct().getPrice());
        }
    }

    private List<ProductEntity> getChangedPriceProducts(CartEntity cart) {
        ArrayList<ProductEntity> products = new ArrayList<>();
        for (CartItemEntity item : cart.getItems()) {
            ProductEntity product = item.getProduct();
            BigDecimal currentProductPrice = item.getProduct().getPrice();
            BigDecimal priceWhenAdded = item.getPriceWhenAdded();
            if (!currentProductPrice.equals(priceWhenAdded)) products.add(product);
        }
        return products;
    }

    private List<ProductEntity> getOutOfStockItems(CartEntity cart) {
        ArrayList<ProductEntity> products = new ArrayList<>();
        for (CartItemEntity item : cart.getItems()) {
            ProductEntity product = item.getProduct();
            Long availableStock = product.getStock().getQuantity();
            Long requestedQuantity = item.getQuantity();
            if (availableStock < requestedQuantity) products.add(product);
        }
        return products;
    }

}
