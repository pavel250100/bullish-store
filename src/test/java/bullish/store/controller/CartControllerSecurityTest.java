package bullish.store.controller;

import bullish.store.assembler.CartModelAssembler;
import bullish.store.assembler.ProductModelAssembler;
import bullish.store.assembler.StockModelAssembler;
import bullish.store.communication.cart.CartAddProductRequest;
import bullish.store.configuration.ApplicationConfig;
import bullish.store.configuration.SecurityConfig;
import bullish.store.configuration.jwt.JwtAuthFilter;
import bullish.store.configuration.jwt.JwtService;
import bullish.store.entity.CartEntity;
import bullish.store.entity.CartItemEntity;
import bullish.store.entity.ProductEntity;
import bullish.store.repository.UserRepository;
import bullish.store.service.cart.CartService;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
@Import({JwtAuthFilter.class, JwtService.class, CartModelAssembler.class, ProductModelAssembler.class, StockModelAssembler.class, SecurityConfig.class, ApplicationConfig.class})
class CartControllerSecurityTest {

    @MockBean
    private CartService cartService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void AddProductShouldBeRestrictedToAdmins() throws Exception {
        CartAddProductRequest request = CartAddProductRequest.builder().productId(1L).quantity(10L).build();
        mockMvc.perform(put("/cart", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    public void AddProductShouldBeRestrictedToAnonymousUser() throws Exception {
        CartAddProductRequest request = CartAddProductRequest.builder().productId(1L).quantity(10L).build();
        mockMvc.perform(put("/cart", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void AddProductShouldBeAvailableToAuthenticatedUser() throws Exception {
        CartAddProductRequest request = CartAddProductRequest.builder().productId(1L).quantity(10L).build();
        CartEntity cart = new CartEntity();
        ProductEntity product = new ProductEntity();
        product.setId(request.getProductId());
        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setProduct(product);
        cartItem.setQuantity(request.getQuantity());
        cart.setItems(new ArrayList<>(List.of(cartItem)));
        doNothing().when(cartService).addProduct(request);
        mockMvc.perform(put("/cart", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

}