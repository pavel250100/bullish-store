package bullish.store.controller;

import bullish.store.assembler.ProductModelAssembler;
import bullish.store.communication.product.Product;
import bullish.store.communication.product.ProductCreateRequest;
import bullish.store.communication.product.ProductUpdateRequest;
import bullish.store.entity.ProductEntity;
import bullish.store.exception.product.ProductConflictException;
import bullish.store.exception.product.ProductNotFoundException;
import bullish.store.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductModelAssembler assembler;

    @GetMapping
    public CollectionModel<EntityModel<Product>> getAll() {
        List<ProductEntity> productEntities = productService.getAll();
        List<Product> dtos = Product.toDtoList(productEntities);
        return assembler.toCollectionModel(dtos);
    }

    @GetMapping("/{id}")
    public EntityModel<Product> getById(@PathVariable Long id) throws ProductNotFoundException {
        ProductEntity productEntity = productService.getById(id);
        Product dto = Product.toDto(productEntity);
        return assembler.toModel(dto);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> create(@RequestBody ProductCreateRequest request) {
        ProductEntity savedProductEntity = productService.create(request);
        Product dto = Product.toDto(savedProductEntity);
        return assembler.toCreated(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public EntityModel<Product> update(
            @PathVariable Long id,
            @RequestBody ProductUpdateRequest request
    ) throws ProductNotFoundException, ProductConflictException {
        ProductEntity updatedProductEntity = productService.update(id, request);
        Product dto = Product.toDto(updatedProductEntity);
        return assembler.toModel(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        productService.removeProductFromStock(id);
        return ResponseEntity.noContent().build();
    }
}
