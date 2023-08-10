package bullish.store.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductStockTest {

    @Autowired
    private TestEntityManager entityManager;

    private Product getDummyProduct() {
        return Product.builder()
                .name("Test Product")
                .desc("Test Description")
                .price(new BigDecimal("99.99"))
                .build();
    }

    @Test
    public void ShouldCreateAndInitializeCreatedAt() {
        Product product = getDummyProduct();
        Product savedProduct = entityManager.persistAndFlush(product);
        assertNotNull(savedProduct.getCreatedAt());
    }

    @Test
    public void ShouldCreateAndFindProduct() {
        Product product = getDummyProduct();
        Product savedProduct = entityManager.persistAndFlush(product);
        Product foundProduct = entityManager.find(Product.class, savedProduct.getId());

        assertNotNull(foundProduct);
        assertEquals(savedProduct.getName(), foundProduct.getName());
        assertEquals(savedProduct.getDesc(), foundProduct.getDesc());
        assertEquals(savedProduct.getPrice(), foundProduct.getPrice());
        assertNotNull(savedProduct.getCreatedAt());
        assertNotNull(savedProduct.getLastUpdatedAt());
    }

    @Test
    public void ShouldUpdateProductAndChangeLastUpdatedAt() {
        Product product = getDummyProduct();
        Product savedProduct = entityManager.persistAndFlush(product);
        Product newProduct = entityManager.find(Product.class, savedProduct.getId());

        assertNotNull(newProduct.getCreatedAt());

        newProduct.setPrice(new BigDecimal("79.99"));
        ZonedDateTime initialLastUpdatedAt = newProduct.getLastUpdatedAt();
        Product updatedProduct = entityManager.persistAndFlush(newProduct);

        assertNotNull(updatedProduct.getLastUpdatedAt());
        assertTrue(updatedProduct.getLastUpdatedAt().isAfter(initialLastUpdatedAt));
    }

    @Test
    public void ShouldCreateStockWheneverProductIsCreated() {
        Product product = getDummyProduct();
        Product savedProduct = entityManager.persistAndFlush(product);

        Product foundProduct = entityManager.find(Product.class, savedProduct.getId());

        assertNotNull(foundProduct);
        assertEquals(savedProduct.getName(), foundProduct.getName());

        Stock foundStock = entityManager.find(Stock.class, savedProduct.getId());
        assertNotNull(foundStock);
        assertEquals(savedProduct, foundStock.getProduct());
        assertEquals(0, foundStock.getQuantity());
    }

    @Test
    public void ShouldDeleteStockWheneverProductIsDeleted() {
        Product product = getDummyProduct();
        Product savedProduct = entityManager.persistAndFlush(product);

        entityManager.remove(savedProduct);
        entityManager.flush();

        Product foundProduct = entityManager.find(Product.class, savedProduct.getId());
        Stock foundStock = entityManager.find(Stock.class, savedProduct.getId());

        assertNull(foundProduct);
        assertNull(foundStock);
    }

    @Test
    public void ShouldUpdateStockLastUpdatedAt() {
        Product product = getDummyProduct();
        Product savedProduct = entityManager.persistAndFlush(product);

        Stock foundStock = entityManager.find(Stock.class, savedProduct.getId());
        ZonedDateTime initialLastUpdatedAt = foundStock.getLastUpdatedAt();

        foundStock.setQuantity(15L);
        Stock updatedStock = entityManager.persistAndFlush(foundStock);

        assertNotNull(updatedStock.getLastUpdatedAt());
        assertTrue(updatedStock.getLastUpdatedAt().isAfter(initialLastUpdatedAt));
    }
}