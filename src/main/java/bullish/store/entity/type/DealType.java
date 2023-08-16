package bullish.store.entity.type;

import bullish.store.entity.CartItemEntity;
import bullish.store.entity.DealEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public enum DealType {

    BUY_N_GET_M_FREE { // Buy 2 products, get 1 free

        @Override
        public BigDecimal calculateDiscount(CartItemEntity item) {
            BigDecimal price = item.getProduct().getPrice();
            DealEntity deal = item.getProduct().getDeal();

            long dealQuantity = deal.getBuyQuantity();
            long freeQuantity = deal.getFreeQuantity();

            long fullPriceQuantity = (item.getQuantity() / (dealQuantity + freeQuantity)) * dealQuantity;
            long remainingQuantity = item.getQuantity() % (dealQuantity + freeQuantity);

            fullPriceQuantity += Math.min(remainingQuantity, dealQuantity);

            BigDecimal totalPrice = price.multiply(BigDecimal.valueOf(item.getQuantity()));
            BigDecimal discountedPrice = price.multiply(BigDecimal.valueOf(fullPriceQuantity));

            return totalPrice.subtract(discountedPrice);
        }
    },
    BUY_N_GET_DISCOUNT { // Buy 2 products, get 50% off
        @Override
        public BigDecimal calculateDiscount(CartItemEntity item) {
            BigDecimal price = item.getProduct().getPrice();
            DealEntity deal = item.getProduct().getDeal();
            BigDecimal discount = deal.getDiscountPercentage();
            BigDecimal discountMultiplier = BigDecimal.ONE.subtract(
                    discount.divide(BigDecimal.valueOf(100), 5, RoundingMode.HALF_UP)
            );

            BigDecimal totalPrice = price.multiply(BigDecimal.valueOf(item.getQuantity()));
            BigDecimal discountedPrice = price.multiply(BigDecimal.valueOf(item.getQuantity())).multiply(discountMultiplier);

            return totalPrice.subtract(discountedPrice);
        }
    },
    BUY_N_GET_DISCOUNT_ON_N_PLUS_1 { // Buy 2, get 50% off the third
        @Override
        public BigDecimal calculateDiscount(CartItemEntity item) {
            BigDecimal price = item.getProduct().getPrice();
            DealEntity deal = item.getProduct().getDeal();
            BigDecimal discount = deal.getDiscountPercentage();
            BigDecimal discountMultiplier = BigDecimal.ONE.subtract(
                    discount.divide(BigDecimal.valueOf(100), 5, RoundingMode.HALF_UP)
            );

            long quantityOrdered = item.getQuantity();
            long numberOfDiscountedItems = quantityOrdered / (deal.getBuyQuantity() + 1);
            long numberOfFullPriceItems = quantityOrdered - numberOfDiscountedItems;

            BigDecimal totalPrice = price.multiply(BigDecimal.valueOf(quantityOrdered));
            BigDecimal discountedPrice = price.multiply(BigDecimal.valueOf(numberOfFullPriceItems))
                    .add(price.multiply(BigDecimal.valueOf(numberOfDiscountedItems))
                            .multiply(discountMultiplier));

            return totalPrice.subtract(discountedPrice);
        }
    };

    public final BigDecimal totalDiscount(CartItemEntity item) {
        if (!isDealApplicable(item)) return BigDecimal.ZERO;
        return calculateDiscount(item);
    };

    protected abstract BigDecimal calculateDiscount(CartItemEntity item);

    public Optional<String> getApplicableDeal(CartItemEntity item) {
        return isDealApplicable(item) ?
                Optional.of(item.getProduct().getDeal().getDealDescription()) :
                Optional.empty();
    }

    public boolean isDealApplicable(CartItemEntity item) {
        DealEntity deal = item.getProduct().getDeal();
        return item.getQuantity() >= (deal.getBuyQuantity());
    }

}
