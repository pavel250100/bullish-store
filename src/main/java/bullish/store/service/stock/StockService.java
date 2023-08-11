package bullish.store.service.stock;

import bullish.store.communication.stock.StockUpdateRequest;
import bullish.store.entity.StockEntity;

import java.util.List;

public interface StockService {

    List<StockEntity> getAll();
    StockEntity update(Long productId, StockUpdateRequest stockUpdateRequest);
    StockEntity getByProductId(Long productId);

}
