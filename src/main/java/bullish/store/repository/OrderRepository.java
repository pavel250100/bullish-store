package bullish.store.repository;

import bullish.store.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query("SELECT o FROM order o WHERE o.user.username = :username")
    List<OrderEntity> findByUsername(@Param("username") String username);

}
