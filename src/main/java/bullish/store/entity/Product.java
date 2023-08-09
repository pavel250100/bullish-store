package bullish.store.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "product")
public @Data class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String desc;
    private BigDecimal price;

    public Product(String name, String desc, BigDecimal price) {
        this.name = name;
        this.desc = desc;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product[" +
                "id = '" + this.id + "', " +
                "name = '" + this.name + "', " +
                "price = '" + this.price + "'" +
                "desc = '" + this.desc + "'" +
                "]";
    }
}