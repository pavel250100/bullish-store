package bullish.store;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {
    private final ProductRepository repository;
    private final ProductModelAssembler assembler;

    ProductController(ProductRepository repository, ProductModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/products")
    CollectionModel<EntityModel<Product>> all() {
        List<Product> products = repository.findAll();
        return assembler.toCollectionModel(products);
    }

    @GetMapping("/products/{id}")
    EntityModel<Product> one(@PathVariable Long id) throws ProductNotFoundException {
        Product product = repository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));

        return assembler.toModel(product);
    }

    @PostMapping("/products")
    ResponseEntity<?> newProduct(@RequestBody Product newProduct) {
        EntityModel<Product> productModel = assembler.toModel(repository.save(newProduct));
        return ResponseEntity
            .created(productModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(productModel);
    }

    @PutMapping("/products/{id}")
    ResponseEntity<?> replaceProduct(
        @RequestBody Product newProduct,
        @PathVariable Long id
    ) {
        Product updatedProduct = repository.findById(id)
            .map(product -> {
                product.setName(newProduct.getName());
                product.setPrice(newProduct.getPrice());
                return repository.save(product);
            })
            .orElseGet(() -> {
                newProduct.setId(id);
                return repository.save(newProduct);
            });

        EntityModel<Product> productModel = assembler.toModel(updatedProduct);

        return ResponseEntity
            .created(productModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(productModel);
    }

    @DeleteMapping("/products/{id}")
    ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
