package bullish.store.communication.stock;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public @Data class StockUpdateDTO {
    private Long productId;
    private Long quantity;
}
