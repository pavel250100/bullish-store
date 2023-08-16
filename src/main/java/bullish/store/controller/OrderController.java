package bullish.store.controller;

import bullish.store.assembler.OrderModelAssembler;
import bullish.store.communication.order.Order;
import bullish.store.entity.OrderEntity;
import bullish.store.service.order.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    public final OrderServiceImpl orderServiceImpl;
    public final OrderModelAssembler assembler;

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    EntityModel<Order> placeOrder() {
        OrderEntity order = orderServiceImpl.placeOrder();
        Order dto = Order.toDto(order);
        return assembler.toModel(dto);
    }

}
