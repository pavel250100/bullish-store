package bullish.store.service.stock;

import bullish.store.communication.stock.StockUpdateRequest;
import bullish.store.entity.StockEntity;
import bullish.store.exception.stock.StockConflictException;
import bullish.store.exception.stock.StockNotFoundException;
import bullish.store.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    @Transactional(readOnly = true)
    @Override
    public List<StockEntity> getAll() {
        return stockRepository.findAll();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public StockEntity update(Long productId, StockUpdateRequest request) {
        StockEntity existingStockEntity = this.getByProductId(productId);
        if (!existingStockEntity.getVersion().equals(request.getVersion())) {
            throw new StockConflictException(existingStockEntity);
        }
        existingStockEntity.setQuantity(request.getQuantity());
        return stockRepository.save(existingStockEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public StockEntity getByProductId(Long productId) {
        return stockRepository.findById(productId)
                .orElseThrow(() -> new StockNotFoundException(productId));
    }
}
