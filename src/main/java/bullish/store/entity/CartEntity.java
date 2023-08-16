package bullish.store.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "cart")
@EqualsAndHashCode(exclude = {"items"})
@Table(name = "carts")
public @Data class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Builder.Default
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CartItemEntity> items = new ArrayList<>();

    public void addItem(CartItemEntity item) {
        item.setCart(this);
        items.add(item);
    }

    public void removeItem(CartItemEntity item) {
        item.setCart(null);
        items.remove(item);
    }

    public BigDecimal totalPrice() {
        return items.stream().map(CartItemEntity::totalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal totalDiscount() {
        return items.stream().map(CartItemEntity::totalDiscount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
