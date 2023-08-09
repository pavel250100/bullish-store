package bullish.store.repository;

import bullish.store.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    // Spring automatically generates implementation for this function
    // as long as name of the function follows naming convention
    Optional<Stock> findByProductId(Long productId);
}
