package bullish.store.service.stock;

import bullish.store.communication.stock.StockUpdateRequest;
import bullish.store.entity.StockEntity;
import bullish.store.exception.stock.StockConflictException;
import bullish.store.exception.stock.StockNotFoundException;
import bullish.store.repository.StockRepository;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockEntityServiceImplTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockServiceImpl stockService;

    StockEntity dummyStock(Long productId, Long version) {
        StockEntity stockEntity = new StockEntity();
        stockEntity.setQuantity(1L);
        stockEntity.setVersion(version);
        stockEntity.setProductId(productId);
        return stockEntity;
    }

    @Test
    public void ShouldReturnEntireStock() {
        StockEntity stockEntity1 = dummyStock(1L, 1L);
        StockEntity stockEntity2 = dummyStock(2L, 1L);
        when(stockRepository.findAll()).thenReturn(Arrays.asList(stockEntity1, stockEntity2));

        List<StockEntity> stockEntities = stockService.getAll();

        assertEquals(2, stockEntities.size());
        verify(stockRepository).findAll();
    }

    @Test
    public void ShouldReturnStock_WhenStockExists() {
        StockEntity stockEntity = dummyStock(1L, 1L);
        when(stockRepository.findById(anyLong())).thenReturn(Optional.of(stockEntity));

        StockEntity foundStockEntity = stockService.getByProductId(1L);

        assertEquals(stockEntity, foundStockEntity);
        verify(stockRepository).findById(anyLong());
    }

    @Test
    public void ShouldThrowException_WhenStockDoesNotExist() {
        when(stockRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(StockNotFoundException.class, () -> stockService.getByProductId(1L));
        assertEquals("Could not find stock for product id 1", exception.getMessage());
        verify(stockRepository).findById(anyLong());
    }

    @Test
    public void ShouldUpdateStock_WhenVersionsMatch() {
        StockEntity existingStockEntity = dummyStock(1L, 20L);
        StockUpdateRequest updateRequest = new StockUpdateRequest(1L, 20L);
        when(stockRepository.findById(anyLong())).thenReturn(Optional.of(existingStockEntity));
        when(stockRepository.save(any(StockEntity.class))).thenReturn(existingStockEntity);

        StockEntity updatedStockEntity = stockService.update(1L, updateRequest);

        assertEquals(updateRequest.getQuantity(), updatedStockEntity.getQuantity());
        verify(stockRepository).findById(anyLong());
        verify(stockRepository).save(any(StockEntity.class));
    }

    @Test
    public void ShouldThrowException_WhenVersionsDontMatch() {
        StockEntity existingStockEntity = dummyStock(1L, 19L);
        StockUpdateRequest updateRequest = new StockUpdateRequest(2L, 20L);
        when(stockRepository.findById(anyLong())).thenReturn(Optional.of(existingStockEntity));

        Exception exception = assertThrows(StockConflictException.class, () -> stockService.update(1L, updateRequest));
        assertEquals("Conflict detected while updating stock for product 1", exception.getMessage());
        verify(stockRepository).findById(anyLong());
    }

}