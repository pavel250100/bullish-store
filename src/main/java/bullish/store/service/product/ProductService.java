package bullish.store.service.product;

import bullish.store.communication.product.ProductCreateRequest;
import bullish.store.communication.product.ProductUpdateRequest;
import bullish.store.entity.ProductEntity;
import bullish.store.exception.product.ProductNotFoundException;

import java.util.List;

public interface ProductService {

        ProductEntity getById(Long id) throws ProductNotFoundException;
        List<ProductEntity> getAll();
        ProductEntity create(ProductCreateRequest request);
        ProductEntity update(Long id, ProductUpdateRequest request);
        void deleteById(Long id);

}
