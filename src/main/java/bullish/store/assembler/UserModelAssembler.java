package bullish.store.assembler;

import bullish.store.communication.user.User;
import bullish.store.controller.UserController;
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
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {

    @Override
    @Nonnull
    public EntityModel<User> toModel(@Nonnull User entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(UserController.class).getUserByUsername(entity.getUsername())).withSelfRel()
        );
    }

    @Override
    @Nonnull
    public CollectionModel<EntityModel<User>> toCollectionModel(@Nonnull Iterable<? extends User> entities) {
        List<EntityModel<User>> stockModels = StreamSupport.stream(entities.spliterator(), false)
                .map(this::toModel)
                .toList();

        return CollectionModel.of(stockModels,
                linkTo(methodOn(UserController.class).getAll()).withSelfRel()
        );
    }
}
