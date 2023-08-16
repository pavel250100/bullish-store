package bullish.store.communication.cart;

import bullish.store.entity.CartItemEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public @Data class CartItem {
    private Long productId;
    private Long quantity;
    private String deal;
    private BigDecimal productPrice;
    private BigDecimal totalDiscount;
    private BigDecimal totalPrice;

    public static CartItem toDto(CartItemEntity entity) {
        return CartItem.builder()
                .productId(entity.getId())
                .quantity(entity.getQuantity())
                .productPrice(entity.getProduct().getPrice())
                .totalDiscount(entity.totalDiscount())
                .totalPrice(entity.totalPrice())
                .deal(entity.getAppliedDeal())
                .build();
    }
}
