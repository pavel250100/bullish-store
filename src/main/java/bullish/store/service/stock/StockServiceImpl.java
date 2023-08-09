package bullish.store.service.stock;

import bullish.store.entity.Stock;
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
    public Stock create(Long productId, Long quantity) {
        Stock stock = new Stock(productId, quantity);
        return stockRepository.save(stock);
    }

    @Override
    public Stock update(Long productId, Long quantity) {
        Stock stock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> new StockNotFoundException("Could not find stock for product id " + productId));
        stock.setQuantity(quantity);
        return stockRepository.save(stock);
    }

    @Override
    public Stock getById(Long id) {
        return stockRepository.findById(id).orElseThrow(() ->
                new StockNotFoundException(id));
    }

    @Override
    public Stock getByProductId(Long productId) {
        return stockRepository.findByProductId(productId)
                .orElseThrow(() -> new StockNotFoundException("Could not find stock for product id " + productId));
    }
}
