package bullish.store.service.product;

import bullish.store.communication.product.ProductCreateRequest;
import bullish.store.communication.product.ProductUpdateRequest;
import bullish.store.entity.ProductEntity;
import bullish.store.exception.product.ProductConflictException;
import bullish.store.exception.product.ProductNotFoundException;
import bullish.store.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    ProductEntity dummyProduct(Long id, String name, String desc, BigDecimal price, Long version) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(name);
        productEntity.setDesc(desc);
        productEntity.setPrice(price);
        productEntity.setId(id);
        productEntity.setVersion(version);
        return productEntity;
    }

    @Test
    public void ShouldReturnSingleProduct() {
        ProductEntity expectedProductEntity = dummyProduct(1L, "Product 1", "Desc 1", BigDecimal.valueOf(100), 1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(expectedProductEntity));

        ProductEntity actualProductEntity = productService.getById(1L);
        assertEquals(expectedProductEntity, actualProductEntity);
        verify(productRepository).findById(1L);
    }

    @Test
    public void ShouldThrowProductNotFoundException_OnFindById() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.getById(1L));
        verify(productRepository).findById(1L);
    }

    @Test
    public void ShouldReturnProductList() {
        List<ProductEntity> expectedProductEntities = Arrays.asList(
                dummyProduct(1L, "Product 1", "Desc 1", BigDecimal.valueOf(100), 1L),
                dummyProduct(2L, "Product 2", "Desc 2", BigDecimal.valueOf(200), 1L)
        );
        when(productRepository.findAll()).thenReturn(expectedProductEntities);
        List<ProductEntity> actualProductEntities = productService.getAll();
        assertEquals(expectedProductEntities, actualProductEntities);
        verify(productRepository).findAll();
    }

    @Test
    public void ShouldReturnCreatedProduct() {
        ProductCreateRequest request = new ProductCreateRequest("Product 1", "Desc 1", BigDecimal.valueOf(100));
        ProductEntity expectedProductEntity = dummyProduct(1L, "Product 1", "Desc 1", BigDecimal.valueOf(100), 1L);
        when(productRepository.save(any())).thenReturn(expectedProductEntity);
        ProductEntity actualProductEntity = productService.create(request);
        assertEquals(expectedProductEntity, actualProductEntity);
    }

    @Test
    public void ShouldReturnUpdatedProduct() {
        ProductUpdateRequest request = new ProductUpdateRequest("New Product 1", "New Desc 1", BigDecimal.valueOf(150), 1L);
        ProductEntity existingProductEntity = dummyProduct(1L, "Product 1", "Desc 1", BigDecimal.valueOf(100), 1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProductEntity));

        ProductEntity actualProductEntity = productService.update(1L, request);

        assertEquals(existingProductEntity, actualProductEntity);
    }

    @Test
    public void ShouldThrowProductConflictException_WhenUpdatingProduct() {
        ProductUpdateRequest request = new ProductUpdateRequest("New Product 1", "New Desc 1", BigDecimal.valueOf(150), 1L);
        ProductEntity existingProductEntity = dummyProduct(1L, "Product 1", "Desc 1", BigDecimal.valueOf(100), 2L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProductEntity));

        assertThrows(ProductConflictException.class, () -> productService.update(1L, request));
        verify(productRepository).findById(1L);
    }

    @Test
    public void ShouldCallDeleteById() {
        productService.deleteById(1L);
        verify(productRepository).deleteById(1L);
    }


}