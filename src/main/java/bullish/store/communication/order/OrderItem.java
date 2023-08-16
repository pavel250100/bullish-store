package bullish.store.communication.order;

import bullish.store.entity.OrderItemEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public @Data class OrderItem {

    private Long quantity;
    private Long productId;
    private String dealApplied;
    private BigDecimal totalPrice;
    private BigDecimal totalDiscount;

    public static OrderItem toDto(OrderItemEntity entity) {
        return OrderItem.builder()
                .quantity(entity.getQuantity())
                .totalPrice(entity.getProduct().getPrice())
                .productId(entity.getProduct().getId())
                .dealApplied(entity.getDealApplied())
                .totalPrice(entity.getTotalPrice())
                .totalDiscount(entity.getTotalDiscount())
                .build();
    }
}
