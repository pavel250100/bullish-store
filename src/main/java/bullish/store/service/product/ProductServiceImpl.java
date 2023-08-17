package bullish.store.service.product;

import bullish.store.communication.product.ProductCreateRequest;
import bullish.store.communication.product.ProductUpdateRequest;
import bullish.store.entity.ProductEntity;
import bullish.store.exception.product.ProductConflictException;
import bullish.store.exception.product.ProductNotFoundException;
import bullish.store.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    @Override
    public ProductEntity getById(Long id) throws ProductNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductEntity> getAll() {
        return productRepository.findAll();
    }

    @Transactional
    @Override
    public ProductEntity create(ProductCreateRequest request) {
        ProductEntity productEntity = ProductEntity.builder()
                .name(request.getName())
                .desc(request.getDesc())
                .price(request.getPrice())
                .build();
        return productRepository.save(productEntity);
    }

    @Transactional
    @Override
    public ProductEntity update(Long id, ProductUpdateRequest request) {
        ProductEntity existingProductEntity = this.getById(id);
        if (request.getVersion() < existingProductEntity.getVersion()) {
            throw new ProductConflictException(existingProductEntity);
        }
        existingProductEntity.setPrice(request.getPrice());
        existingProductEntity.setDesc(request.getDesc());
        existingProductEntity.setName(request.getName());
        return existingProductEntity;
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
