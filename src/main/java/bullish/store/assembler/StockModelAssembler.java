package bullish.store.assembler;

import bullish.store.controller.ProductController;
import bullish.store.controller.StockController;
import bullish.store.entity.Stock;
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
    public EntityModel<Stock> toModel(@Nonnull Stock stock) {
        return EntityModel.of(stock,
                linkTo(methodOn(StockController.class).one(stock.getId())).withSelfRel(),
                linkTo(methodOn(ProductController.class).one(stock.getProductId())).withRel("product"),
                linkTo(methodOn(StockController.class).all()).withRel("stock")
        );
    }

    @Override
    @Nonnull
    public CollectionModel<EntityModel<Stock>> toCollectionModel(@Nonnull Iterable<? extends Stock> stocks) {
        List<EntityModel<Stock>> stockModels = StreamSupport.stream(stocks.spliterator(), false)
                .map(this::toModel)
                .toList();

        return CollectionModel.of(stockModels,
                linkTo(methodOn(ProductController.class).all()).withSelfRel()
        );
    }

}
