package bullish.store.service.product;

import bullish.store.entity.Product;
import bullish.store.entity.Stock;
import bullish.store.exception.ProductHasBeenChangedException;
import bullish.store.exception.ProductNotFoundException;
import bullish.store.repository.ProductRepository;
import bullish.store.service.stock.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
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
        return productRepository.save(newProduct);
    }

    @Override
    public Product update(Long id, Product newProduct) {
        Product existingProduct = this.getById(id);
        if (existingProduct.hashCode() != newProduct.hashCode()) {
            throw new ProductHasBeenChangedException(id);
        }
        existingProduct.setPrice(newProduct.getPrice());
        existingProduct.setDesc(newProduct.getDesc());
        existingProduct.setName(newProduct.getName());
        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
