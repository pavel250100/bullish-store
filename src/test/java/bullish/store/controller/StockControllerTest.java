package bullish.store.controller;

import bullish.store.assembler.StockModelAssembler;
import bullish.store.entity.Stock;
import bullish.store.repository.StockRepository;
import bullish.store.service.stock.StockServiceImpl;
import bullish.store.service.stock.StockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StockController.class)
class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockRepository stockRepository;

    @TestConfiguration
    static class ProductControllerTestContextConfiguration {

        @Autowired
        private StockRepository stockRepository;

        @Bean
        public StockModelAssembler stockModelAssembler() {
            return new StockModelAssembler();
        }

        @Bean
        public StockService stockService() {
            return new StockServiceImpl(stockRepository);
        }
    }

    @Test
    void ShouldReturnStockById() throws Exception {
        Stock stock = new Stock();
        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
        mockMvc.perform(get("/stock/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.productId", equalTo(2)))
                .andExpect(jsonPath("$.quantity", equalTo(10)));
    }

    @Test
    void OnGetStockByProductId_ShouldHandleStockNotFoundByProductId_And_ReturnNotFoundMessage() throws Exception {
        Long nonExistentProductId = 1L;
        when(stockRepository.findById(nonExistentProductId)).thenReturn(Optional.empty());
        mockMvc.perform(
                        get("/stock/product/{id}", nonExistentProductId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Could not find stock for product id " + nonExistentProductId));
    }

    @Test
    void OnGetStockById_ShouldHandleStockNotFoundById_And_ReturnNotFoundMessage() throws Exception {
        Long nonExistentStockId = 1L;
        when(stockRepository.findById(nonExistentStockId)).thenReturn(Optional.empty());
        mockMvc.perform(
                        get("/stock/{id}", nonExistentStockId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Could not find stock " + nonExistentStockId));
    }

    @Test
    void ShouldAttachCorrectLinksToSingleStock() throws Exception {
        Stock stock = new Stock();
        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
        mockMvc.perform(get("/stock/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href", equalTo("http://localhost/stock/1")))
                .andExpect(jsonPath("$._links.product.href", equalTo("http://localhost/products/2")))
                .andExpect(jsonPath("$._links.stock.href", equalTo("http://localhost/stock")))
                .andExpect(jsonPath("$._links.*", hasSize(3)));
    }

    @Test
    void ShouldReturnStockByProductId() throws Exception {
        Stock stock = new Stock();
//        when(stockRepository.findByProductId(2L)).thenReturn(Optional.of(stock));
        mockMvc.perform(get("/stock/product/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)));
    }

    @Test
    void ShouldReturnAllStocks() throws Exception {
        List<Stock> stocks = new ArrayList<>();
        stocks.add(new Stock());
        stocks.add(new Stock());

        when(stockRepository.findAll()).thenReturn(stocks);

        mockMvc.perform(get("/stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.stockList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.stockList[0].id", equalTo(1)))
                .andExpect(jsonPath("$._embedded.stockList[1].id", equalTo(2)));
    }
}