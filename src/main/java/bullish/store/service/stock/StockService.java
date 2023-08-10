package bullish.store.service.stock;

import bullish.store.entity.Stock;

import java.util.List;

public interface StockService {

    List<Stock> getAll();
    Stock update(Long productId, Stock newStock);
    Stock getByProductId(Long productId);

}
