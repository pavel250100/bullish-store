package bullish.store.assembler;

import bullish.store.communication.order.Order;
import jakarta.annotation.Nonnull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {

    @Override
    @Nonnull
    public EntityModel<Order> toModel(@Nonnull Order order) {
        return EntityModel.of(order);
    }

}
