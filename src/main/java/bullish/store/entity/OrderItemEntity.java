package bullish.store.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "order_item")
@Table(name = "order_items")
@EqualsAndHashCode(exclude = {"order", "product"})
public @Data class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "price_when_ordered")
    private BigDecimal priceWhenOrdered;

    private String dealApplied;
    private BigDecimal totalPrice;
    private BigDecimal totalDiscount;

}
