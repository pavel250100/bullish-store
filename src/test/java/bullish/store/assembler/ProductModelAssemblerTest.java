package bullish.store.assembler;

import bullish.store.communication.product.Product;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProductModelAssemblerTest {

    private final ProductModelAssembler assembler = new ProductModelAssembler();

    @Test
    void ShouldConvertSingleProduct() {
        Product product = new Product();
        product.setId(1L);

        EntityModel<Product> entityModel = assembler.toModel(product);

        assertThat(entityModel.getContent()).isEqualTo(product);
        assertThat(entityModel.getLinks()).hasSize(3);
        assertThat(entityModel.getRequiredLink(IanaLinkRelations.SELF).getHref()).endsWith("/products/" + product.getId());
        assertThat(entityModel.getRequiredLink("products").getHref()).endsWith("/products");
        assertThat(entityModel.getRequiredLink("stock").getHref()).endsWith("/stock/" + product.getId());
    }

    @Test
    void ShouldConvertModelCollection() {
        Product product1 = new Product();
        product1.setId(1L);

        Product product2 = new Product();
        product2.setId(2L);

        List<Product> dtos = List.of(product1, product2);

        CollectionModel<EntityModel<Product>> collectionModel = assembler.toCollectionModel(dtos);

        assertThat(collectionModel.getContent()).hasSize(2);
        assertThat(collectionModel.getLinks()).hasSize(1);
        assertThat(collectionModel.getRequiredLink(IanaLinkRelations.SELF).getHref()).endsWith("/products");
    }

    @Test
    @SuppressWarnings("unchecked")
    void ShouldConvertToCreatedResponse() {
        Product product = new Product();
        product.setId(1L);

        ResponseEntity<?> responseEntity = assembler.toCreated(product);
        EntityModel<Product> entityModel = (EntityModel<Product>) responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNotNull(entityModel);
        assertThat(entityModel.getContent()).isEqualTo(product);
        assertThat(entityModel.getLinks()).hasSize(3);
        assertThat(entityModel.getRequiredLink(IanaLinkRelations.SELF).getHref()).endsWith("/products/" + product.getId());
        assertThat(entityModel.getRequiredLink("products").getHref()).endsWith("/products");
        assertThat(entityModel.getRequiredLink("stock").getHref()).endsWith("/stock/" + product.getId());
    }

}