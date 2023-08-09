package bullish.store.assembler;

import bullish.store.controller.ProductController;
import bullish.store.controller.StockController;
import bullish.store.entity.Product;
import jakarta.annotation.Nonnull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductModelAssembler implements RepresentationModelAssembler<Product, EntityModel<Product>> {

    @Override
    @Nonnull
    public EntityModel<Product> toModel(@Nonnull Product product) {
        return EntityModel.of(product,
            linkTo(methodOn(ProductController.class).one(product.getId())).withSelfRel(),
            linkTo(methodOn(ProductController.class).all()).withRel("products"),
            linkTo(methodOn(StockController.class).getByProductId(product.getId())).withRel("stock")
        );
    }

    @Override
    @Nonnull
    public CollectionModel<EntityModel<Product>> toCollectionModel(@Nonnull Iterable<? extends Product> products) {
        List<EntityModel<Product>> productModels = StreamSupport.stream(products.spliterator(), false)
            .map(this::toModel)
            .toList();

        return CollectionModel.of(productModels,
            linkTo(methodOn(ProductController.class).all()).withSelfRel()
        );
    }

    public ResponseEntity<?> toCreated(Product savedProduct) {
        EntityModel<Product> productModel = toModel(savedProduct);
        return ResponseEntity
                .created(productModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(productModel);
    }

}
