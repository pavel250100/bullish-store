package bullish.store.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "order")
@Table(name = "orders")
public @Data class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> items = new ArrayList<>();

    private ZonedDateTime orderDate;
    private BigDecimal totalPrice;
    private BigDecimal totalDiscount;

    @PrePersist
    protected void onCreate() {
        orderDate = ZonedDateTime.now(ZoneOffset.UTC);
    }

    public void addOrderItem(OrderItemEntity orderItem) {
        orderItem.setOrder(this);
        items.add(orderItem);
    }
}
