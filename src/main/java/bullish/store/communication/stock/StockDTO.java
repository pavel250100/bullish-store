package bullish.store.communication.stock;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public @Data class StockDTO {
    private Long productId;
    private Long quantity;
    private ZonedDateTime lastUpdatedAt;
}
