package bullish.store.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductStockEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private ProductEntity getDummyProduct() {
        return ProductEntity.builder()
                .name("Test Product")
                .desc("Test Description")
                .price(new BigDecimal("99.99"))
                .build();
    }

    @Test
    public void ShouldCreateAndInitializeCreatedAt() {
        ProductEntity productEntity = getDummyProduct();
        ProductEntity savedProductEntity = entityManager.persistAndFlush(productEntity);
        assertNotNull(savedProductEntity.getCreatedAt());
    }

    @Test
    public void ShouldCreateAndFindProduct() {
        ProductEntity productEntity = getDummyProduct();
        ProductEntity savedProductEntity = entityManager.persistAndFlush(productEntity);
        ProductEntity foundProductEntity = entityManager.find(ProductEntity.class, savedProductEntity.getId());

        assertNotNull(foundProductEntity);
        assertEquals(savedProductEntity.getName(), foundProductEntity.getName());
        assertEquals(savedProductEntity.getDesc(), foundProductEntity.getDesc());
        assertEquals(savedProductEntity.getPrice(), foundProductEntity.getPrice());
        assertEquals(savedProductEntity.getVersion(), 0L);
        assertNotNull(savedProductEntity.getCreatedAt());
        assertNotNull(savedProductEntity.getLastUpdatedAt());
    }

    @Test
    public void ShouldUpdateProductAndChangeLastUpdatedAtAndVersion() {
        ProductEntity productEntity = getDummyProduct();
        ProductEntity savedProductEntity = entityManager.persistAndFlush(productEntity);
        ProductEntity newProductEntity = entityManager.find(ProductEntity.class, savedProductEntity.getId());

        assertNotNull(savedProductEntity.getCreatedAt());
        assertEquals(savedProductEntity.getVersion(), 0L);
        assertEquals(savedProductEntity.getStockEntity().getVersion(), 0L);

        newProductEntity.setPrice(new BigDecimal("79.99"));
        ZonedDateTime initialLastUpdatedAt = newProductEntity.getLastUpdatedAt();
        ProductEntity updatedProductEntity = entityManager.persistAndFlush(newProductEntity);

        assertNotNull(updatedProductEntity.getLastUpdatedAt());
        assertTrue(updatedProductEntity.getLastUpdatedAt().isAfter(initialLastUpdatedAt));
        assertEquals(updatedProductEntity.getVersion(), 1L);
        assertEquals(savedProductEntity.getStockEntity().getVersion(), 0L);
    }

    @Test
    public void ShouldCreateStockWheneverProductIsCreated() {
        ProductEntity productEntity = getDummyProduct();
        ProductEntity savedProductEntity = entityManager.persistAndFlush(productEntity);

        ProductEntity foundProductEntity = entityManager.find(ProductEntity.class, savedProductEntity.getId());

        assertNotNull(foundProductEntity);
        assertEquals(savedProductEntity.getName(), foundProductEntity.getName());

        StockEntity foundStockEntity = entityManager.find(StockEntity.class, savedProductEntity.getId());
        assertNotNull(foundStockEntity);
        assertEquals(savedProductEntity, foundStockEntity.getProductEntity());
        assertEquals(0, foundStockEntity.getQuantity());
    }

    @Test
    public void ShouldDeleteStockWheneverProductIsDeleted() {
        ProductEntity productEntity = getDummyProduct();
        ProductEntity savedProductEntity = entityManager.persistAndFlush(productEntity);

        entityManager.remove(savedProductEntity);
        entityManager.flush();

        ProductEntity foundProductEntity = entityManager.find(ProductEntity.class, savedProductEntity.getId());
        StockEntity foundStockEntity = entityManager.find(StockEntity.class, savedProductEntity.getId());

        assertNull(foundProductEntity);
        assertNull(foundStockEntity);
    }

    @Test
    public void ShouldUpdateStockLastUpdatedAtAndVersion() {
        ProductEntity productEntity = getDummyProduct();
        ProductEntity savedProductEntity = entityManager.persistAndFlush(productEntity);

        StockEntity newStockEntity = entityManager.find(StockEntity.class, savedProductEntity.getId());
        assertEquals(newStockEntity.getVersion(), 0L);
        assertEquals(newStockEntity.getProductEntity().getVersion(), 0L);
        ZonedDateTime initialLastUpdatedAt = newStockEntity.getLastUpdatedAt();

        newStockEntity.setQuantity(15L);
        StockEntity updatedStockEntity = entityManager.persistAndFlush(newStockEntity);
        assertEquals(updatedStockEntity.getVersion(), 1L);
        assertEquals(updatedStockEntity.getProductEntity().getVersion(), 0L);

        assertNotNull(updatedStockEntity.getLastUpdatedAt());
        assertTrue(updatedStockEntity.getLastUpdatedAt().isAfter(initialLastUpdatedAt));
    }
}