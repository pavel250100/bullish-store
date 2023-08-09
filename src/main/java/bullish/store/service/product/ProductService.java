package bullish.store.service.product;

import bullish.store.entity.Product;
import bullish.store.exception.ProductNotFoundException;

import java.util.List;

public interface ProductService {

        Product getById(Long id) throws ProductNotFoundException;
        List<Product> getAll();
        Product create(Product newProduct);
        Product update(Long id, Product newProduct);
        void deleteById(Long id);

}
