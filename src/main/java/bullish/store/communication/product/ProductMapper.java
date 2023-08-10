package bullish.store.communication.product;

import bullish.store.entity.Product;

public class ProductMapper {

    public static ProductDTO toDto(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .desc(product.getDesc())
                .price(product.getPrice())
                .createdAt(product.getCreatedAt())
                .lastUpdatedAt(product.getLastUpdatedAt())
                .build();
    }

    public static Product toProduct(ProductDTO product) {
        return Product.builder()
                .id(product.getId())
                .name(product.getName())
                .desc(product.getDesc())
                .price(product.getPrice())
                .createdAt(product.getCreatedAt())
                .lastUpdatedAt(product.getLastUpdatedAt())
                .build();
    }

}
