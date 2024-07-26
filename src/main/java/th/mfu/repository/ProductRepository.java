package th.mfu.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import th.mfu.domain.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findAll();

}

