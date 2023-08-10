package bullish.store.service.product;

import bullish.store.entity.Product;
import bullish.store.exception.ProductHasBeenChangedException;
import bullish.store.exception.ProductNotFoundException;
import bullish.store.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest
class ProductServiceImplTest {

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ProductServiceImpl productService;

    @Test
    public void ShouldReturnProductById() {
        Long productId = 1L;
        Product product = new Product();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Product result = productService.getById(productId);

        assertEquals(product, result);
    }

    @Test
    public void ShouldThrowProductNotFoundExceptionOnGetById() {
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ProductNotFoundException.class, () -> productService.getById(productId));

        String expectedMessage = "Could not find product 1";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void ShouldReturnAllProducts() {
        Product product1 = new Product();
        Product product2 = new Product();
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<Product> result = productService.getAll();

        assertEquals(2, result.size());
    }

    @Test
    public void ShouldCreateNewProductAndReturnCreatedProduct() {
        Product newProduct = new Product();
        when(productRepository.save(newProduct)).thenReturn(newProduct);

        Product result = productService.create(newProduct);

        assertEquals(newProduct, result);
    }

    @Test
    public void ShouldUpdateProductWithNoConflict() {
        Long productId = 1L;
        Product existingProduct = new Product();
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        Product newProduct = new Product();

        productService.update(productId, newProduct);

        verify(productRepository).save(existingProduct);
    }

    @Test
    public void ShouldThrowProductHasBeenChangedExceptionOnUpdate() {
        Long productId = 1L;
        Product existingProduct = new Product();
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        Product newProduct = new Product();
        newProduct.setName("New Name");

        Exception exception = assertThrows(ProductHasBeenChangedException.class, () -> productService.update(productId, newProduct));
        String expectedMessage = "Product 1 has been changed";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void testDeleteById() {
        Long productId = 1L;

        productService.deleteById(productId);

        verify(productRepository).deleteById(productId);
    }


}