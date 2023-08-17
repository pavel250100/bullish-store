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
    private Long version;

    public static Product toDto(ProductEntity product) {
        return Product.builder()
                .id(product.getId())
                .name(product.getName())
                .desc(product.getDesc())
                .price(product.getPrice())
                .deal(product.getDealDescription())
                .createdAt(product.getCreatedAt())
                .lastUpdatedAt(product.getLastUpdatedAt())
                .version(product.getVersion())
                .build();
    }

    public static List<Product> toDtoList(List<ProductEntity> productEntities) {
        return productEntities.stream()
                .map(Product::toDto)
                .collect(Collectors.toList());
    }
}
