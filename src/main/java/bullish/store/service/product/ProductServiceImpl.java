package bullish.store.service.product;

import bullish.store.entity.Product;
import bullish.store.entity.Stock;
import bullish.store.exception.ProductNotFoundException;
import bullish.store.repository.ProductRepository;
import bullish.store.service.stock.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final StockService stockService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, StockService stockService) {
        this.productRepository = productRepository;
        this.stockService = stockService;
    }

    @Override
    public Product getById(Long id) throws ProductNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Product create(Product newProduct) {
        Product product = productRepository.save(newProduct);
        stockService.create(product.getId(), 0L);
        return product;
    }

    @Override
    public Product update(Long id, Product newProduct) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(newProduct.getName());
                    product.setPrice(newProduct.getPrice());
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
