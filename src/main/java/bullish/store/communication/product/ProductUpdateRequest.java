package bullish.store.communication.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public @Data class ProductUpdateRequest {
    private String name;
    private String desc;
    private BigDecimal price;
    private Long version;
}
