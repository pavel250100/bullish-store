package bullish.store.controller;

import bullish.store.assembler.CartModelAssembler;
import bullish.store.communication.cart.Cart;
import bullish.store.communication.cart.CartAddProductRequest;
import bullish.store.entity.CartEntity;
import bullish.store.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartModelAssembler assembler;

    @PutMapping
    @PreAuthorize("hasRole('USER')")
    public EntityModel<Cart> addProduct(@RequestBody CartAddProductRequest request) {
        CartEntity cartEntity = cartService.addProduct(request);
        Cart cart = Cart.toDto(cartEntity);
        return assembler.toModel(cart);
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('USER')")
    public EntityModel<Cart> removeProduct(@PathVariable Long productId) {
        CartEntity cartEntity = cartService.removeProduct(productId);
        Cart cart = Cart.toDto(cartEntity);
        return assembler.toModel(cart);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public EntityModel<Cart> getCart() {
        CartEntity cartEntity = cartService.getCart();
        Cart cart = Cart.toDto(cartEntity);
        return assembler.toModel(cart);
    }

}
