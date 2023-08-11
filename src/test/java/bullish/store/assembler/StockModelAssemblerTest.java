package bullish.store.assembler;

import bullish.store.communication.stock.Stock;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StockModelAssemblerTest {

    private final StockModelAssembler assembler = new StockModelAssembler();

    @Test
    void ShouldConvertToSingleStockModel() {
        Stock stock = new Stock();
        stock.setProductId(1L);

        EntityModel<Stock> entityModel = assembler.toModel(stock);

        assertThat(entityModel.getContent()).isEqualTo(stock);
        assertThat(entityModel.getLinks()).hasSize(3);
        assertThat(entityModel.getRequiredLink(IanaLinkRelations.SELF).getHref()).endsWith("/stock/" + stock.getProductId());
        assertThat(entityModel.getRequiredLink("product").getHref()).endsWith("/products/" + stock.getProductId());
        assertThat(entityModel.getRequiredLink("stock").getHref()).endsWith("/stock");
    }

    @Test
    void ShouldConvertToStockCollection() {
        Stock stock1 = new Stock();
        stock1.setProductId(1L);

        Stock stock2 = new Stock();
        stock2.setProductId(2L);

        List<Stock> dtos = List.of(stock1, stock2);

        CollectionModel<EntityModel<Stock>> collectionModel = assembler.toCollectionModel(dtos);

        assertThat(collectionModel.getContent()).hasSize(2);
        assertThat(collectionModel.getLinks()).hasSize(1);
        assertThat(collectionModel.getRequiredLink(IanaLinkRelations.SELF).getHref()).endsWith("/stock");
    }
}