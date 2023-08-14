package bullish.store.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Table(name = "products")
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "product")
public @Data class ProductEntity {

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

    @Version
    private Long version;

    @OneToOne(
            mappedBy = "productEntity",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private StockEntity stockEntity;

    @Builder(toBuilder = true)
    public ProductEntity(String name, String desc, BigDecimal price) {
        this.name = name;
        this.desc = desc;
        this.price = price;
        createStock();
    }

    public ProductEntity() {
        createStock();
    }

    private void createStock() {
        StockEntity stockEntity = new StockEntity();
        stockEntity.setProductEntity(this);
        stockEntity.setQuantity(0L);
        this.stockEntity = stockEntity;
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
                "price = '" + this.price + "', " +
                "desc = '" + this.desc + "', " +
                "createdAt = '" + this.createdAt + "', " +
                "lastUpdatedAt = '" + this.lastUpdatedAt + "', " +
                "version = '" + this.version + "'" +
                "]";
    }
}