package bullish.store.communication.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public @Data class CartAddProductRequest {
    private Long productId;
    private Long quantity;
}
