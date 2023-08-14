package bullish.store.controller;

import bullish.store.assembler.ProductModelAssembler;
import bullish.store.assembler.StockModelAssembler;
import bullish.store.assembler.UserModelAssembler;
import bullish.store.communication.stock.StockUpdateRequest;
import bullish.store.configuration.ApplicationConfig;
import bullish.store.configuration.SecurityConfig;
import bullish.store.configuration.jwt.JwtAuthFilter;
import bullish.store.configuration.jwt.JwtService;
import bullish.store.entity.StockEntity;
import bullish.store.repository.UserRepository;
import bullish.store.service.stock.StockService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StockController.class)
@Import({JwtAuthFilter.class, JwtService.class, ProductModelAssembler.class, StockModelAssembler.class, UserModelAssembler.class, SecurityConfig.class, ApplicationConfig.class})
class StockControllerSecurityTest {

    @MockBean
    private StockService stockService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @NonNull
    private StockEntity dummyStock() {
        StockEntity stock = new StockEntity();
        stock.setProductId(1L);
        stock.setVersion(1L);
        stock.setQuantity(10L);
        return stock;
    }

    @Test
    @WithAnonymousUser
    void AnybodyShouldBeAbleToAccessGetAllStocks() throws Exception {
        mockMvc.perform(get("/stock")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void AnybodyShouldBeAbleToAccessGetStockByProductId() throws Exception {
        when(stockService.getByProductId(1L)).thenReturn(dummyStock());
        mockMvc.perform(get("/stock/{productId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void UpdateStockShouldBeRestrictedFromUsers() throws Exception {
        StockUpdateRequest request = new StockUpdateRequest(10L, 1L);
        mockMvc.perform(put("/stock/{productId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void UpdateStockShouldBeAvailableToAdmins() throws Exception {
        StockUpdateRequest request = new StockUpdateRequest(10L, 1L);
        when(stockService.update(1L, request)).thenReturn(dummyStock());
        mockMvc.perform(put("/stock/{productId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

}