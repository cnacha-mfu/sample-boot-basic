package th.mfu.repository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

import th.mfu.domain.Saleorder;

import java.util.Collection;;

public interface SaleOrderRepository extends CrudRepository<Saleorder, Long> {
    public List<Saleorder> findAll();

   
    public List<Saleorder> findByCustomerId(Long customerId);
    public List<Saleorder> findByCustomerName(String name);
}
