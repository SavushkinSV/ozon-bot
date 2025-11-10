package ssv.home.ozonbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssv.home.ozonbot.entity.product.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
