package bullish.store.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

@AllArgsConstructor
@Builder
@Table(name = "product")
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "product")
public @Data class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String desc;
    private BigDecimal price;

    @CreatedDate
    private ZonedDateTime createdAt;

    @LastModifiedDate
    private ZonedDateTime lastUpdatedAt;

    @OneToOne(
            mappedBy = "product",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Stock stock;

    @Builder
    public Product(String name, String desc, BigDecimal price) {
        this.name = name;
        this.desc = desc;
        this.price = price;
        createStock();
    }

    @SuppressWarnings("unused")
    // overriding builder construction in order to initialize Stock to 0
    public static class ProductBuilder {
        public Product build() {
            Product product = new Product(name, desc, price);
            product.setStock(new Stock());
            product.getStock().setProduct(product);
            product.getStock().setQuantity(0L);
            return product;
        }
    }

    public Product() {
        createStock();
    }

    private void createStock() {
        this.stock = Stock.builder()
                .quantity(0L)
                .product(this)
                .build();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = lastUpdatedAt = ZonedDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdatedAt = ZonedDateTime.now();
    }

    @Override
    public String toString() {
        return "Product[" +
                "id = '" + this.id + "', " +
                "name = '" + this.name + "', " +
                "price = '" + this.price + "'" +
                "desc = '" + this.desc + "'" +
                "createdAt = '" + this.createdAt + "', " +
                "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, desc, price);
    }
}