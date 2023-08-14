package bullish.store.assembler;

import bullish.store.communication.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserModelAssemblerTest {

    private final UserModelAssembler assembler = new UserModelAssembler();

    @Test
    void ShouldConvertToSingleUserModel() {
        User user = User.builder().username("test-user").build();

        EntityModel<User> entityModel = assembler.toModel(user);

        assertThat(entityModel.getContent()).isEqualTo(user);
        assertThat(entityModel.getLinks()).hasSize(1);
        assertThat(entityModel.getRequiredLink(IanaLinkRelations.SELF).getHref()).endsWith("/users/" + user.getUsername());
    }

    @Test
    void ShouldConvertToStockCollection() {
        User user1 = User.builder().username("test-user-1").build();
        User user2 = User.builder().username("test-user-2").build();

        List<User> dtos = List.of(user1, user2);

        CollectionModel<EntityModel<User>> collectionModel = assembler.toCollectionModel(dtos);

        assertThat(collectionModel.getContent()).hasSize(2);
        assertThat(collectionModel.getLinks()).hasSize(1);
        assertThat(collectionModel.getRequiredLink(IanaLinkRelations.SELF).getHref()).endsWith("/users");
    }

}