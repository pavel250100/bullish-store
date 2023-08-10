package bullish.store.service.stock;

import bullish.store.entity.Stock;
import bullish.store.exception.StockHasBeenChangedException;
import bullish.store.exception.StockNotFoundException;
import bullish.store.repository.StockRepository;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest
class StockServiceImplTest {

    @MockBean
    private StockRepository stockRepository;

    @Autowired
    private StockServiceImpl stockService;

    @Test
    public void ShouldReturnEntireStock() {
        Stock stock1 = new Stock();
        Stock stock2 = new Stock();
        when(stockRepository.findAll()).thenReturn(Arrays.asList(stock1, stock2));

        List<Stock> stocks = stockService.getAll();

        assertEquals(2, stocks.size());
        verify(stockRepository, times(1)).findAll();
    }

    @Test
    public void ShouldUpdateStockWithNoConflict() {
        Long productId = 1L;
        Stock existingStock = new Stock();
        existingStock.setQuantity(10L);
        when(stockRepository.findById(productId)).thenReturn(Optional.of(existingStock));

        Stock newStock = new Stock();
        newStock.setQuantity(10L);

        stockService.update(productId, newStock);

        verify(stockRepository).save(existingStock);
    }

    @Test
    public void ShouldThrowStockHasBeenChangedExceptionOnConflict() {
        Long productId = 1L;
        Stock existingStock = new Stock();
        existingStock.setQuantity(10L);
        when(stockRepository.findById(productId)).thenReturn(Optional.of(existingStock));

        Stock newStock = new Stock();
        newStock.setQuantity(20L);

        Exception exception = assertThrows(StockHasBeenChangedException.class, () -> stockService.update(productId, newStock));

        String expectedMessage = "Stock for product 1 has been changed";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void ShouldReturnStockByProductId() {
        Stock stock = new Stock();
        stock.setProductId(1L);

        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
        Stock foundStock = stockService.getByProductId(1L);
        assertEquals(1L, foundStock.getProductId());
    }

    @Test
    public void ShouldThrowNotFoundExceptionWhenStockNotFound() {
        when(stockRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(StockNotFoundException.class, () -> stockService.getByProductId(1L));

        String expectedMessage = "Could not find stock for product id 1";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

}