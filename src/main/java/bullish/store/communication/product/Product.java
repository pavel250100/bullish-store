package bullish.store.communication.product;

import bullish.store.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public @Data class Product {
    private Long id;
    private String name;
    private String desc;
    private BigDecimal price;
    private ZonedDateTime createdAt;
    private ZonedDateTime lastUpdatedAt;
    private String deal;

    public static Product toDto(ProductEntity productEntity) {
        return Product.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .desc(productEntity.getDesc())
                .price(productEntity.getPrice())
                .deal(productEntity.getDealDescription())
                .createdAt(productEntity.getCreatedAt())
                .lastUpdatedAt(productEntity.getLastUpdatedAt())
                .build();
    }

    public static List<Product> toDtoList(List<ProductEntity> productEntities) {
        return productEntities.stream()
                .map(Product::toDto)
                .collect(Collectors.toList());
    }
}
