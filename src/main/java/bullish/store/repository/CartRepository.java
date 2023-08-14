package bullish.store.repository;

import bullish.store.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {

    @Query("SELECT c FROM cart c JOIN c.user u WHERE u.username = :username")
    Optional<CartEntity> findByUsername(@Param("username") String username);

}
