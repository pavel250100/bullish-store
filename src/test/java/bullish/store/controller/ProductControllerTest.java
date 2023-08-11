package bullish.store.controller;

import bullish.store.assembler.ProductModelAssembler;
import bullish.store.assembler.StockModelAssembler;
import bullish.store.communication.product.ProductCreateRequest;
import bullish.store.communication.product.ProductUpdateRequest;
import bullish.store.entity.ProductEntity;
import bullish.store.exception.product.ProductConflictException;
import bullish.store.exception.product.ProductNotFoundException;
import bullish.store.service.product.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class ProductControllerTestContextConfiguration {
        @Bean
        public ProductModelAssembler productModelAssembler() {
            return new ProductModelAssembler();
        }
        @Bean
        public StockModelAssembler stockModelAssembler() { return new StockModelAssembler(); }
    }

    @Test
    void ShouldReturnStatusOkAndJsonProductList() throws Exception {
        List<ProductEntity> productEntities = Arrays.asList(
                new ProductEntity("Product 1", "Description 1", BigDecimal.ONE),
                new ProductEntity("Product 2", "Description 2", BigDecimal.ONE)
        );

        when(productService.getAll()).thenReturn(productEntities);

        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded.productList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.productList[0].name", equalTo("Product 1")))
                .andExpect(jsonPath("$._embedded.productList[0].desc", equalTo("Description 1")))
                .andExpect(jsonPath("$._embedded.productList[0].price", equalTo(1)))
                .andExpect(jsonPath("$._embedded.productList[1].name", equalTo("Product 2")))
                .andExpect(jsonPath("$._embedded.productList[1].desc", equalTo("Description 2")))
                .andExpect(jsonPath("$._embedded.productList[1].price", equalTo(1)));
    }

    @Test
    void ShouldReturnStatusCreatedAndCreatedProduct() throws Exception {
        ProductCreateRequest createRequest = new ProductCreateRequest("New Product", "New Description", BigDecimal.TEN, 1L);

        ProductEntity savedProduct = new ProductEntity("New Product", "New Description", BigDecimal.TEN);

        when(productService.create(createRequest)).thenReturn(savedProduct);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.name", equalTo("New Product")))
                .andExpect(jsonPath("$.desc", equalTo("New Description")))
                .andExpect(jsonPath("$.price", equalTo(10)));
    }

    @Test
    void ShouldReturnStatusOkAndUpdatedProduct() throws Exception {
        ProductUpdateRequest updateRequest = new ProductUpdateRequest("Product", "Description", BigDecimal.TEN, 1L);
        ProductEntity savedProduct = new ProductEntity("New Product", "New Description", BigDecimal.TEN);

        Long productId = 1L;
        when(productService.update(1L, updateRequest)).thenReturn(savedProduct);

        mockMvc.perform(put("/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.name", equalTo("New Product")))
                .andExpect(jsonPath("$.desc", equalTo("New Description")))
                .andExpect(jsonPath("$.price", equalTo(10)));
    }

    @Test
    void ShouldReturnStatusOkAndProduct_OnGetById() throws Exception {
        Long productId = 1L;
        ProductEntity savedProduct = new ProductEntity("New Product", "New Description", BigDecimal.TEN);
        when(productService.getById(1L)).thenReturn(savedProduct);
        mockMvc.perform(get("/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.name", equalTo("New Product")))
                .andExpect(jsonPath("$.desc", equalTo("New Description")))
                .andExpect(jsonPath("$.price", equalTo(10)));
    }

    @Test
    void ShouldReturnProductNotFoundException_OnGetById() throws Exception {
        Long nonExistentProductId = 1L;

        when(productService.getById(nonExistentProductId)).thenThrow(new ProductNotFoundException(nonExistentProductId));

        mockMvc.perform(get("/products/{id}", nonExistentProductId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Could not find product " + nonExistentProductId));
    }

    @Test
    void ShouldReturnProductNotFoundException_OnUpdate() throws Exception {
        ProductUpdateRequest updateRequest = new ProductUpdateRequest("Product", "Description", BigDecimal.TEN, 1L);
        Long nonExistentProductId = 1L;

        when(productService.update(nonExistentProductId, updateRequest)).thenThrow(new ProductNotFoundException(nonExistentProductId));

        mockMvc.perform(put("/products/{id}", nonExistentProductId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Could not find product " + nonExistentProductId));
    }

    @Test
    void ShouldReturnProductConflictExceptionMessageAndLatestProduct_OnUpdate() throws Exception {
        ProductUpdateRequest updateRequest = new ProductUpdateRequest("Product 2", "Description 2", BigDecimal.TEN, 1L);
        ProductEntity latestProductEntity = new ProductEntity();
        latestProductEntity.setVersion(2L);
        latestProductEntity.setName("Product 2");
        latestProductEntity.setPrice(BigDecimal.TEN);
        latestProductEntity.setDesc("Description 2");

        Long nonExistentProductId = 1L;

        when(productService.update(nonExistentProductId, updateRequest)).thenThrow(new ProductConflictException(latestProductEntity));

        mockMvc.perform(put("/products/{id}", nonExistentProductId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isConflict())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.name", equalTo("Product 2")))
                .andExpect(jsonPath("$.desc", equalTo("Description 2")))
                .andExpect(jsonPath("$.price", equalTo(10)));
    }

    @Test
    void ShouldDeleteProductAndReturnStatusNoContent() throws Exception {
        Long productId = 1L;

        doNothing().when(productService).deleteById(productId);

        mockMvc.perform(delete("/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

}