package bullish.store.assembler;

import bullish.store.communication.stock.Stock;
import bullish.store.controller.ProductController;
import bullish.store.controller.StockController;
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
public class StockModelAssembler implements RepresentationModelAssembler<Stock, EntityModel<Stock>> {

    @Override
    @Nonnull
    public EntityModel<Stock> toModel(@Nonnull Stock entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(StockController.class).getByProductId(entity.getProductId())).withSelfRel(),
                linkTo(methodOn(ProductController.class).getById(entity.getProductId())).withRel("product"),
                linkTo(methodOn(StockController.class).getAll()).withRel("stock")
        );
    }

    @Override
    @Nonnull
    public CollectionModel<EntityModel<Stock>> toCollectionModel(@Nonnull Iterable<? extends Stock> entities) {
        List<EntityModel<Stock>> stockModels = StreamSupport.stream(entities.spliterator(), false)
                .map(this::toModel)
                .toList();

        return CollectionModel.of(stockModels,
                linkTo(methodOn(StockController.class).getAll()).withSelfRel()
        );
    }
}
