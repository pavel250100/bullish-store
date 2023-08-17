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
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> addProduct(@RequestBody CartAddProductRequest request) {
        cartService.addProductToCart(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> removeProduct(@PathVariable Long productId) {
        cartService.removeProductFromCart(productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public EntityModel<Cart> getCart() {
        CartEntity cartEntity = cartService.getCart();
        Cart cart = Cart.toDto(cartEntity);
        return assembler.toModel(cart);
    }

}
