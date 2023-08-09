package bullish.store.controller;

import bullish.store.assembler.ProductModelAssembler;
import bullish.store.exception.ProductNotFoundException;
import bullish.store.entity.Product;
import bullish.store.service.product.ProductService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final ProductModelAssembler assembler;

    ProductController(ProductService productService, ProductModelAssembler assembler) {
        this.productService = productService;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<Product>> all() {
        List<Product> products = productService.getAll();
        return assembler.toCollectionModel(products);
    }

    @GetMapping("/{id}")
    public EntityModel<Product> one(@PathVariable Long id) throws ProductNotFoundException {
        Product product = productService.getById(id);
        return assembler.toModel(product);
    }

    @PostMapping
    ResponseEntity<?> create(@RequestBody Product newProduct) {
        Product savedProduct = productService.create(newProduct);
        return assembler.toCreated(savedProduct);
    }

    @PutMapping("/{id}")
    ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody Product newProduct
    ) {
        Product updatedProduct = productService.update(id, newProduct);
        return assembler.toCreated(updatedProduct);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteById(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
