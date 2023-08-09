package bullish.store.controller;

import bullish.store.assembler.ProductModelAssembler;
import bullish.store.entity.Product;
import bullish.store.entity.Stock;
import bullish.store.repository.ProductRepository;
import bullish.store.service.product.ProductService;
import bullish.store.service.product.ProductServiceImpl;
import bullish.store.service.stock.StockService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private StockService stockService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class ProductControllerTestContextConfiguration {

        @Autowired
        private ProductRepository productRepository;

        @Autowired
        private StockService stockService;

        @Bean
        public ProductModelAssembler productModelAssembler() {
            return new ProductModelAssembler();
        }

        @Bean
        public ProductService productService() {
            return new ProductServiceImpl(productRepository, stockService);
        }
    }

    @Test
    public void ShouldReturnSingleProduct() throws Exception {
        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setName("Product 1");
        existingProduct.setPrice(new BigDecimal(100));

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));

        mockMvc.perform(get("/products/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.name", equalTo("Product 1")))
                .andExpect(jsonPath("$.price", equalTo(100)));
    }

    @Test
    public void ShouldAttachCorrectLinksToSingleProduct() throws Exception {
        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setName("Product 1");
        existingProduct.setPrice(new BigDecimal(100));

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));

        mockMvc.perform(get("/products/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href", equalTo("http://localhost/products/1")))
                .andExpect(jsonPath("$._links.products.href", equalTo("http://localhost/products")))
                .andExpect(jsonPath("$._links.stock.href", equalTo("http://localhost/stock/product/1")))
                .andExpect(jsonPath("$._links.*", hasSize(3)));
    }

    @Test
    public void OnGetSingleProduct_ShouldHandleNotFoundProduct_And_ReturnNotFoundMessage() throws Exception {
        Long nonExistentProductId = 1L;
        when(productRepository.findById(nonExistentProductId)).thenReturn(Optional.empty());
        mockMvc.perform(
                        get("/products/{id}", nonExistentProductId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Could not find product " + nonExistentProductId));
    }

    @Test
    public void ShouldReturnAllProducts() throws Exception {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");
        product1.setPrice(new BigDecimal(100));

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setPrice(new BigDecimal(200));

        List<Product> productList = List.of(product1, product2);

        when(productRepository.findAll()).thenReturn(productList);

        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.productList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.productList[0].id", equalTo(1)))
                .andExpect(jsonPath("$._embedded.productList[0].name", equalTo("Product 1")))
                .andExpect(jsonPath("$._embedded.productList[0].price", equalTo(100)))
                .andExpect(jsonPath("$._embedded.productList[1].id", equalTo(2)))
                .andExpect(jsonPath("$._embedded.productList[1].name", equalTo("Product 2")))
                .andExpect(jsonPath("$._embedded.productList[1].price", equalTo(200)));
    }

    @Test
    public void ShouldAttachLinksToProductCollectionCorrectly() throws Exception {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");
        product1.setPrice(new BigDecimal(100));

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setPrice(new BigDecimal(200));

        List<Product> productList = List.of(product1, product2);

        when(productRepository.findAll()).thenReturn(productList);

        mockMvc.perform(
                        get("/products")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href", equalTo("http://localhost/products")))
                .andExpect(jsonPath("$._links.*", hasSize(1)));
    }

    @Test
    public void CreateNewProduct_And_ReturnCreatedProduct() throws Exception {
        Product newProduct = new Product();
        newProduct.setName("Product 1");
        newProduct.setPrice(new BigDecimal(100));

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName(newProduct.getName());
        savedProduct.setPrice(newProduct.getPrice());

        when(stockService.create(anyLong(), anyLong())).thenReturn(new Stock());
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        mockMvc.perform(
                        post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(newProduct))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.name", equalTo("Product 1")))
                .andExpect(jsonPath("$.price", equalTo(100)));
    }

    @Test
    public void UpdateProduct_And_ReturnUpdatedProduct() throws Exception {
        Long productId = 1L;
        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Existing Product");
        existingProduct.setPrice(new BigDecimal(100));

        Product newProduct = new Product();
        newProduct.setName("New Product");
        newProduct.setPrice(new BigDecimal(200));

        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setName(newProduct.getName());
        updatedProduct.setPrice(newProduct.getPrice());

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        mockMvc.perform(
                        put("/products/{id}", productId)
                                .content(objectMapper.writeValueAsString(newProduct))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo("New Product")))
                .andExpect(jsonPath("$.price", equalTo(200)));
    }

    @Test
    public void OnUpdate_ShouldHandleNotFoundProduct_And_ReturnNotFoundMessage() throws Exception {
        Long nonExistentProductId = 1L;
        Product newProduct = new Product();
        when(productRepository.findById(nonExistentProductId)).thenReturn(Optional.empty());
        mockMvc.perform(
                        put("/products/{id}", nonExistentProductId)
                                .content(objectMapper.writeValueAsString(newProduct))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().string("Could not find product " + nonExistentProductId));
    }

    @Test
    public void ShouldDeleteProduct() throws Exception {
        Long productId = 1L;
        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Product");
        existingProduct.setPrice(new BigDecimal(100));

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        doNothing().when(productRepository).delete(existingProduct);

        mockMvc.perform(
                        delete("/products/{id}", productId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(productRepository, times(1)).deleteById(existingProduct.getId());
    }
}