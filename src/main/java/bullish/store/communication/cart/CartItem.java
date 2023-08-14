package bullish.store.communication.cart;

import bullish.store.entity.CartItemEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public @Data class CartItem {
    private Long productId;
    private Long quantity;

    public static CartItem toDto(CartItemEntity entity) {
        return CartItem.builder()
                .productId(entity.getId())
                .quantity(entity.getQuantity())
                .build();
    }
}
