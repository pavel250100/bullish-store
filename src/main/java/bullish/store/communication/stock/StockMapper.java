package bullish.store.communication.stock;

import bullish.store.entity.Stock;

public class StockMapper {

    public static StockDTO toDto(Stock stock) {
        return StockDTO.builder()
                .quantity(stock.getQuantity())
                .productId(stock.getProductId())
                .lastUpdatedAt(stock.getLastUpdatedAt())
                .build();
    }

    public static Stock toStock(Stock stock) {
        return Stock.builder()
                .quantity(stock.getQuantity())
                .productId(stock.getProductId())
                .lastUpdatedAt(stock.getLastUpdatedAt())
                .build();
    }

}
