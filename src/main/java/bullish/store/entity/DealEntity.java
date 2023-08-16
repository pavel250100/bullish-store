package bullish.store.entity;

import bullish.store.entity.type.DealType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity(name = "deal")
@Table(name = "deals")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "product")
public @Data class DealEntity {

    @Id
    private Long product_id;

    @Enumerated(EnumType.STRING)
    private DealType dealType;

    private Long buyQuantity;

    private Long freeQuantity;

    private BigDecimal discountPercentage;

    @MapsId
    @OneToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    public String getDealDescription() {
        return switch (dealType) {
            case BUY_N_GET_DISCOUNT -> "Buy " + buyQuantity + " get " + discountPercentage + "% discount";
            case BUY_N_GET_M_FREE -> "Buy " + buyQuantity + " get " + freeQuantity + " free";
            case BUY_N_GET_DISCOUNT_ON_N_PLUS_1 ->
                    "Buy " + buyQuantity + " get " + discountPercentage + "% off on " + buyQuantity + "th product";
        };
    }
}
