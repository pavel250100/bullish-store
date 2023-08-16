package bullish.store.entity;


import bullish.store.entity.type.DealType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "cart_item")
@Table(name = "cart_items")
public @Data class CartItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private CartEntity cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    private BigDecimal priceWhenAdded;

    private Long quantity;

    public BigDecimal totalPrice() {
        BigDecimal totalPriceWithNoDiscount = product.getPrice()
                .multiply(BigDecimal.valueOf(this.quantity))
                .setScale(5, RoundingMode.HALF_UP);

        return totalPriceWithNoDiscount.subtract(totalDiscount())
                .setScale(5, RoundingMode.HALF_UP);
    }

    public BigDecimal totalDiscount() {
        return dealExists() ?
                product.getDeal().getDealType().totalDiscount(this) :
                BigDecimal.ZERO;
    }

    public String getAppliedDeal() {
        if (dealExists()) {
            DealType dealType = product.getDeal().getDealType();
            Optional<String> applicableDeal = dealType.getApplicableDeal(this);
            return applicableDeal.orElse("No Deal Applied");
        }
        return "No Deal Available";
    }

    public boolean dealExists() {
        return product.getDeal() != null;
    }
}
