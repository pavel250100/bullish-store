package bullish.store.assembler;

import bullish.store.communication.cart.Cart;
import bullish.store.controller.CartController;
import jakarta.annotation.Nonnull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CartModelAssembler implements RepresentationModelAssembler<Cart, EntityModel<Cart>> {

    @Override
    @Nonnull
    public EntityModel<Cart> toModel(@Nonnull Cart entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(CartController.class).getCart()).withSelfRel()
        );
    }

}