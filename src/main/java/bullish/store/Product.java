package bullish.store;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Product {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private BigDecimal price;

    public Product() {}

    Product (String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Product[" +
                "id = '" + this.id + "', " +
                "name = '" + this.name + "', " +
                "price = '" + this.price + "'" +
                "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.price);
    }
}