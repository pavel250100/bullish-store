package bullish.store.communication.stock;

import bullish.store.entity.StockEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public @Data class Stock {
    private Long productId;
    private Long quantity;
    private ZonedDateTime lastUpdatedAt;
    private Long version;

    public static Stock toDto(StockEntity stockEntity) {
        return Stock.builder()
                .productId(stockEntity.getProductId())
                .quantity(stockEntity.getQuantity())
                .lastUpdatedAt(stockEntity.getLastUpdatedAt())
                .version(stockEntity.getVersion())
                .build();
    }

    public static List<Stock> toDtoList(List<StockEntity> stockEntities) {
        return stockEntities.stream()
                .map(Stock::toDto)
                .collect(Collectors.toList());
    }
}
