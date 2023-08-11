package bullish.store.communication.stock;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public @Data class StockUpdateRequest {
    @Nonnull private Long quantity;
    @Nonnull private Long version;
}
