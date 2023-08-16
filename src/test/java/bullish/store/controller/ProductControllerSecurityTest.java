package bullish.store.controller;

import bullish.store.assembler.ProductModelAssembler;
import bullish.store.assembler.StockModelAssembler;
import bullish.store.assembler.UserModelAssembler;
import bullish.store.communication.product.ProductCreateRequest;
import bullish.store.communication.product.ProductUpdateRequest;
import bullish.store.configuration.ApplicationConfig;
import bullish.store.configuration.SecurityConfig;
import bullish.store.configuration.jwt.JwtAuthFilter;
import bullish.store.configuration.jwt.JwtService;
import bullish.store.entity.ProductEntity;
import bullish.store.repository.UserRepository;
import bullish.store.service.product.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@Import({JwtAuthFilter.class, JwtService.class, ProductModelAssembler.class, StockModelAssembler.class, UserModelAssembler.class, SecurityConfig.class, ApplicationConfig.class})
class ProductControllerSecurityTest {

    @MockBean
    private ProductService productService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductEntity dummyProduct() {
        return ProductEntity.builder()
                .name("product")
                .price(BigDecimal.TEN)
                .desc("desc")
                .build();
    }

    @Test
    @WithAnonymousUser
    public void GetAllProductsShouldBeAvailableToAll() throws Exception {
        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void GetProductByIdShouldBeAvailableToAll() throws Exception {
        when(productService.getById(1L)).thenReturn(dummyProduct());
        mockMvc.perform(get("/products/{productId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void CreateProductShouldBeRestrictedToUsers() throws Exception {
        ProductCreateRequest request =
                ProductCreateRequest.builder().name("product").desc("desc").price(BigDecimal.TEN).build();
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void CreateProductShouldBeAllowedToAdmins() throws Exception {
        ProductCreateRequest request =
                ProductCreateRequest.builder().name("product").desc("desc").price(BigDecimal.TEN).build();
        when(productService.create(request)).thenReturn(dummyProduct());
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithAnonymousUser
    public void UpdateProductShouldBeRestrictedToUsers() throws Exception {
        ProductUpdateRequest request = ProductUpdateRequest.builder().name("name").price(BigDecimal.TEN).version(1L).build();
        when(productService.update(1L, request)).thenReturn(dummyProduct());
        mockMvc.perform(put("/products/{productId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void UpdateProductShouldBeAvailableToAdmins() throws Exception {
        ProductUpdateRequest request = ProductUpdateRequest.builder().name("name").price(BigDecimal.TEN).version(1L).build();
        when(productService.update(1L, request)).thenReturn(dummyProduct());
        mockMvc.perform(put("/products/{productId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void DeleteProductShouldBeRestrictedToUsers() throws Exception {
        doNothing().when(productService).deleteById(any());
        mockMvc.perform(delete("/products/{productId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void DeleteProductShouldBeAvailableToAdmins() throws Exception {
        doNothing().when(productService).deleteById(any());
        mockMvc.perform(delete("/products/{productId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

}