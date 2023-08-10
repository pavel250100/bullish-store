package bullish.store.communication.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public @Data class ProductDTO {
    private Long id;
    private String name;
    private String desc;
    private BigDecimal price;
    private ZonedDateTime createdAt;
    private ZonedDateTime lastUpdatedAt;
}
