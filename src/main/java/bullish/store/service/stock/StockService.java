package bullish.store.service.stock;

import bullish.store.entity.Stock;

import java.util.List;

public interface StockService {

    List<Stock> getAll();
    Stock create(Long productId, Long quantity);
    Stock update(Long productId, Long quantity);
    Stock getById(Long id);
    Stock getByProductId(Long productId);

}
