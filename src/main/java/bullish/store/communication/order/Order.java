package bullish.store.communication.order;

import bullish.store.entity.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public @Data class Order {

    private BigDecimal totalPrice;
    private BigDecimal totalDiscount;
    private List<OrderItem> items;

    public static Order toDto(OrderEntity entity) {
        return Order.builder()
                .items(entity.getItems().stream().map(OrderItem::toDto).collect(Collectors.toList()))
                .totalPrice(entity.getTotalPrice())
                .totalDiscount(entity.getTotalDiscount())
                .build();
    }

}
