package bullish.store.service.stock;

import bullish.store.entity.Stock;
import bullish.store.exception.StockHasBeenChangedException;
import bullish.store.exception.StockNotFoundException;
import bullish.store.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    @Autowired
    public StockServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public List<Stock> getAll() {
        return stockRepository.findAll();
    }

    @Override
    public Stock update(Long productId, Stock newStock) {
        Stock existingStock = this.getByProductId(productId);
        if (existingStock.hashCode() != newStock.hashCode()) {
            throw new StockHasBeenChangedException(productId);
        }
        existingStock.setQuantity(newStock.getQuantity());
        return stockRepository.save(existingStock);
    }

    @Override
    public Stock getByProductId(Long productId) {
        return stockRepository.findById(productId)
                .orElseThrow(() -> new StockNotFoundException(productId));
    }
}
