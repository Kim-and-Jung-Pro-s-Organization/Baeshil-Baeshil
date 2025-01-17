package pro.baeshilbaeshil.application.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "select * from product p where p.id > :id limit :pageSize", nativeQuery = true)
    List<Product> findAllByCursor(@Param("id") Long id, @Param("pageSize") int pageSize);
}
