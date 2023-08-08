package bullish.store;

import jakarta.annotation.Nonnull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
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
            linkTo(methodOn(ProductController.class).all()).withRel("products")
        );
    }

    @Override
    @Nonnull
    public CollectionModel<EntityModel<Product>> toCollectionModel(Iterable<? extends Product> products) {
        List<EntityModel<Product>> productModels = StreamSupport.stream(products.spliterator(), false)
            .map(this::toModel)
            .toList();

        return CollectionModel.of(productModels,
            linkTo(methodOn(ProductController.class).all()).withSelfRel()
        );
    }

}
