package bullish.store.communication.cart;

import bullish.store.entity.CartEntity;
import bullish.store.entity.CartItemEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public @Data class Cart {
    private List<CartItem> items;
    private BigDecimal totalPrice;
    private BigDecimal totalDiscount;

    public static Cart toDto(CartEntity cartEntity) {
        List<CartItem> items = new ArrayList<>();
        for (CartItemEntity item : cartEntity.getItems()) {
            items.add(CartItem.toDto(item));
        }
        return Cart.builder()
                .items(items)
                .totalPrice(cartEntity.totalPrice())
                .totalDiscount(cartEntity.totalDiscount())
                .build();
    }
}
