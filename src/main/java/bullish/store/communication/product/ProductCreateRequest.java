package bullish.store.communication.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
public @Data class ProductCreateRequest {
    private String name;
    private String desc;
    private BigDecimal price;
    private Long version;
}
