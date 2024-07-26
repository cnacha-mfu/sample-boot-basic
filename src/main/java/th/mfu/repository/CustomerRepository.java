package th.mfu.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import th.mfu.domain.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long>{
    List<Customer> findAll();
    List<Customer> findByName(String name);
}

