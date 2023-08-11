package bullish.store.controller;

import bullish.store.assembler.ProductModelAssembler;
import bullish.store.assembler.StockModelAssembler;
import bullish.store.communication.stock.StockUpdateRequest;
import bullish.store.entity.StockEntity;
import bullish.store.exception.stock.StockConflictException;
import bullish.store.exception.stock.StockNotFoundException;
import bullish.store.service.stock.StockService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StockController.class)
class StockControllerTest {

    @MockBean
    private StockService stockService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class ProductControllerTestContextConfiguration {
        @Bean
        public StockModelAssembler stockModelAssembler() { return new StockModelAssembler(); }
        @Bean
        public ProductModelAssembler productModelAssembler() { return new ProductModelAssembler(); }
    }

    private StockEntity dummyStock() {
        StockEntity stock = new StockEntity();
        stock.setProductId(1L);
        stock.setVersion(1L);
        stock.setQuantity(10L);
        return stock;
    }

    @Test
    void ShouldReturnStatusOkAndStock_OnGetByProductId() throws Exception {
        Long productId = 1L;
        StockEntity stock = dummyStock();
        when(stockService.getByProductId(productId)).thenReturn(stock);
        mockMvc.perform(get("/stock/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.productId", equalTo(1)))
                .andExpect(jsonPath("$.quantity", equalTo(10)))
                .andExpect(jsonPath("$.version", equalTo(1)));
    }

    @Test
    void ShouldReturnEntireStock_OnGetAll() throws Exception {
        StockEntity stock1 = dummyStock();
        stock1.setProductId(1L);
        StockEntity stock2 = dummyStock();
        stock2.setProductId(2L);
        List<StockEntity> stocks = Arrays.asList(stock1, stock2);
        when(stockService.getAll()).thenReturn(stocks);

        mockMvc.perform(get("/stock")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded.stockList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.stockList[0].productId", equalTo(1)))
                .andExpect(jsonPath("$._embedded.stockList[0].quantity", equalTo(10)))
                .andExpect(jsonPath("$._embedded.stockList[0].version", equalTo(1)))
                .andExpect(jsonPath("$._embedded.stockList[1].productId", equalTo(2)))
                .andExpect(jsonPath("$._embedded.stockList[1].quantity", equalTo(10)))
                .andExpect(jsonPath("$._embedded.stockList[1].version", equalTo(1)));
    }

    @Test
    void ShouldReturnStatusNotFoundAndExceptionMessage_OnGetByProductId() throws Exception {
        Long nonExistentProductId = 1L;
        when(stockService.getByProductId(nonExistentProductId)).thenThrow(new StockNotFoundException(nonExistentProductId));
        mockMvc.perform(get("/stock/{id}", nonExistentProductId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Could not find stock for product id " + nonExistentProductId));
    }

    @Test
    void ShouldReturnUpdatedStockAndStatusOk_OnUpdate() throws Exception {
        StockUpdateRequest request = new StockUpdateRequest(10L, 1L);
        Long productId = 1L;
        when(stockService.update(productId, request)).thenReturn(dummyStock());
        mockMvc.perform(put("/stock/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.productId", equalTo(1)))
                .andExpect(jsonPath("$.quantity", equalTo(10)))
                .andExpect(jsonPath("$.version", equalTo(1)));
    }

    @Test
    void ShouldReturnStatusNotFoundAndExceptionMessage_OnUpdate() throws Exception {
        StockUpdateRequest request = new StockUpdateRequest(10L, 1L);
        Long nonExistentProductId = 1L;
        when(stockService.update(nonExistentProductId, request)).thenThrow(new StockNotFoundException(nonExistentProductId));
        mockMvc.perform(put("/stock/{productId}", nonExistentProductId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Could not find stock for product id " + nonExistentProductId));
    }

    @Test
    void ShouldReturnStatusConflictAndLatestStock_OnUpdate() throws Exception {
        StockEntity latestStock = dummyStock();
        latestStock.setVersion(2L);
        latestStock.setQuantity(20L);
        StockUpdateRequest request = new StockUpdateRequest(10L, 1L);
        Long productId = 1L;
        when(stockService.update(productId, request)).thenThrow(new StockConflictException(latestStock));
        mockMvc.perform(put("/stock/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.productId", equalTo(1)))
                .andExpect(jsonPath("$.quantity", equalTo(20)))
                .andExpect(jsonPath("$.version", equalTo(2)));
    }

}