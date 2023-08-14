package bullish.store.assembler;

import bullish.store.communication.cart.Cart;
import bullish.store.communication.cart.CartItem;
import bullish.store.controller.CartController;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

class CartModelAssemblerTest {
    private final CartModelAssembler assembler = new CartModelAssembler();

    @Test
    public void ShouldAssembleCartWithLinks() {
        CartItem cartItem = CartItem.builder().productId(1L).quantity(10L).build();
        Cart cart = Cart.builder().items(List.of(cartItem)).build();
        EntityModel<Cart> model = assembler.toModel(cart);

        assertNotNull(model);
        assertEquals(cart, model.getContent());
        assertTrue(model.hasLink("self"));
        assertEquals(
                linkTo(methodOn(CartController.class).getCart()).withSelfRel().toUri(),
                model.getRequiredLink("self").toUri()
        );
    }
}