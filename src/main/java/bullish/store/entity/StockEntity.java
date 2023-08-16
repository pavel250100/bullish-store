package bullish.store.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@NoArgsConstructor
@Table(name = "stock")
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "stock")
public @Data class StockEntity {

    @Id
    private Long productId;
    private Long quantity;

    @LastModifiedDate
    private ZonedDateTime lastUpdatedAt;

    @Version
    private Long version;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @PrePersist
    protected void onCreate() {
        lastUpdatedAt = ZonedDateTime.now(ZoneOffset.UTC);
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdatedAt = ZonedDateTime.now(ZoneOffset.UTC);
    }

    @Override
    public String toString() {
        return "Stock[" +
                "productId = '" + this.productId + "', " +
                "quantity = '" + this.quantity + "', " +
                "lastUpdatedAt = '" + this.lastUpdatedAt + "', " +
                "version = '" + this.version + "'" +
                "]";
    }
}
