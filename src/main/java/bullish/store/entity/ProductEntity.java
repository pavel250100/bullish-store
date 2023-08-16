package bullish.store.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "products")
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "product")
@EqualsAndHashCode(exclude = {"stock", "deal"})
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
            mappedBy = "product",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private StockEntity stock;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    private DealEntity deal;

    @Builder(toBuilder = true)
    public ProductEntity(String name, String desc, BigDecimal price) {
        this.name = name;
        this.desc = desc;
        this.price = price;
        createStock();
    }

    public void setStock(StockEntity stock) {
        stock.setProduct(this);
        this.stock = stock;
    }

    public ProductEntity() {
        createStock();
    }

    private void createStock() {
        StockEntity stockEntity = new StockEntity();
        stockEntity.setProduct(this);
        stockEntity.setQuantity(0L);
        this.stock = stockEntity;
    }

    public String getDealDescription() {
        if (deal != null) {
            return deal.getDealDescription();
        }
        return "No deal available";
    }

    @PrePersist
    protected void onCreate() {
        createdAt = lastUpdatedAt = ZonedDateTime.now(ZoneOffset.UTC);
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdatedAt = ZonedDateTime.now(ZoneOffset.UTC);
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