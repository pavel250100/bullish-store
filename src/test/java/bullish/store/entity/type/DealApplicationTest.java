package bullish.store.entity.type;

import bullish.store.entity.CartItemEntity;
import bullish.store.entity.DealEntity;
import bullish.store.entity.ProductEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DealApplicationTest {

    @Test
    public void ShouldApplyBuy2Get1FreeCorrectly() {
        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setQuantity(2L);

        ProductEntity product = new ProductEntity();
        product.setPrice(BigDecimal.valueOf(100));

        DealEntity deal = new DealEntity();
        deal.setProduct(product);
        deal.setDealType(DealType.BUY_N_GET_M_FREE);
        deal.setBuyQuantity(2L);
        deal.setFreeQuantity(1L);
        product.setDeal(deal);
        cartItem.setProduct(product);

        BigDecimal totalPrice = cartItem.totalPrice();
        BigDecimal expectedPrice = BigDecimal.valueOf(200).setScale(5, RoundingMode.HALF_UP);

        assertEquals(totalPrice, expectedPrice);
    }

    @Test
    public void ShouldCalculateFullPrice_AndNotApplyBuy3Get1Free_WhenNotEnoughInOrder() {
        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setQuantity(2L);

        ProductEntity product = new ProductEntity();
        product.setPrice(BigDecimal.valueOf(100));

        DealEntity deal = new DealEntity();
        deal.setProduct(product);
        deal.setDealType(DealType.BUY_N_GET_M_FREE);
        deal.setBuyQuantity(3L); // should buy 3 to get 1 free
        deal.setFreeQuantity(1L);
        product.setDeal(deal);
        cartItem.setProduct(product);

        BigDecimal totalPrice = cartItem.totalPrice();
        BigDecimal expectedPrice = BigDecimal.valueOf(200).setScale(5, RoundingMode.HALF_UP);

        assertEquals(totalPrice, expectedPrice);
    }

    @Test
    public void ShouldApplyBuy5GetDiscountCorrectly() {
        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setQuantity(5L);

        ProductEntity product = new ProductEntity();
        product.setPrice(BigDecimal.valueOf(100));

        DealEntity deal = new DealEntity();
        deal.setProduct(product);
        deal.setDealType(DealType.BUY_N_GET_DISCOUNT);
        deal.setBuyQuantity(5L);
        deal.setDiscountPercentage(BigDecimal.valueOf(50));
        product.setDeal(deal);
        cartItem.setProduct(product);

        BigDecimal totalPrice = cartItem.totalPrice();
        BigDecimal expectedPrice = BigDecimal.valueOf(250).setScale(5, RoundingMode.HALF_UP);

        assertEquals(totalPrice, expectedPrice);
    }

    @Test
    public void ShouldCalculateFullPriceAndNotApplyBuy5GetDiscount_WhenNotEnoughInOrder() {
        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setQuantity(4L); // not enough for a deal

        ProductEntity product = new ProductEntity();
        product.setPrice(BigDecimal.valueOf(100));

        DealEntity deal = new DealEntity();
        deal.setProduct(product);
        deal.setDealType(DealType.BUY_N_GET_DISCOUNT);
        deal.setBuyQuantity(5L);
        deal.setDiscountPercentage(BigDecimal.valueOf(50));
        product.setDeal(deal);
        cartItem.setProduct(product);

        BigDecimal totalPrice = cartItem.totalPrice();
        BigDecimal expectedPrice = BigDecimal.valueOf(400).setScale(5, RoundingMode.HALF_UP);

        assertEquals(totalPrice, expectedPrice);
    }

    @Test
    public void ShouldApplyGet2GetDiscountOnThirdProduct() {
        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setQuantity(3L); // 2 products for full price, third for 50% off

        ProductEntity product = new ProductEntity();
        product.setPrice(BigDecimal.valueOf(100));

        DealEntity deal = new DealEntity();
        deal.setProduct(product);
        deal.setDealType(DealType.BUY_N_GET_DISCOUNT_ON_N_PLUS_1);
        deal.setBuyQuantity(2L);
        deal.setDiscountPercentage(BigDecimal.valueOf(50));
        product.setDeal(deal);
        cartItem.setProduct(product);

        BigDecimal totalPrice = cartItem.totalPrice();
        BigDecimal expectedPrice = BigDecimal.valueOf(250).setScale(5, RoundingMode.HALF_UP);

        assertEquals(totalPrice, expectedPrice);
    }

    @Test
    public void ShouldNotApplyDiscountOnFourthItem_WhenOrder2() {
        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setQuantity(2L); // only 2 products, not 3

        ProductEntity product = new ProductEntity();
        product.setPrice(BigDecimal.valueOf(100));

        DealEntity deal = new DealEntity();
        deal.setProduct(product);
        deal.setDealType(DealType.BUY_N_GET_DISCOUNT_ON_N_PLUS_1);
        deal.setBuyQuantity(3L); // need to buy 3
        deal.setDiscountPercentage(BigDecimal.valueOf(50));
        product.setDeal(deal);
        cartItem.setProduct(product);

        BigDecimal totalPrice = cartItem.totalPrice();
        BigDecimal expectedPrice = BigDecimal.valueOf(200).setScale(5, RoundingMode.HALF_UP);

        assertEquals(totalPrice, expectedPrice);
    }

}