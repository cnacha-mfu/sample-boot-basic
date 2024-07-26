package th.mfu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import th.mfu.domain.Customer;
import th.mfu.domain.Saleorder;
import th.mfu.dto.SaleorderDTO;
import th.mfu.dto.mapper.SaleorderMapper;
import th.mfu.repository.CustomerRepository;
import th.mfu.repository.ProductRepository;
import th.mfu.repository.SaleOrderRepository;


@RestController
public class SaleOrderController {

    @Autowired
    SaleOrderRepository orderRepo;

    @Autowired
    CustomerRepository customerRepo;

    @Autowired
    ProductRepository productRepo;

    @Autowired
    SaleorderMapper saleOrderMapper;

    // POST for creating an order
    @PostMapping("/customers/{customerId}/orders")
    public ResponseEntity<String> createOrder(@PathVariable Long customerId, @RequestBody SaleorderDTO orderDTO) {
        Optional<Customer> customer = customerRepo.findById(customerId);
        if (customer.isPresent()) {
            Saleorder newOrder = new Saleorder();
            saleOrderMapper.updateSaleorderFromDto(orderDTO, newOrder);
            newOrder.setCustomer(customer.get());
            orderRepo.save(newOrder);
            return new ResponseEntity<String>("Order created", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<String>("Customer not found", HttpStatus.NOT_FOUND);
        }
    }


 // GET for getting orders by customer
 @GetMapping("/customers/{customerId}/orders")
 public ResponseEntity<Collection<SaleorderDTO>> getOrdersByCustomer(@PathVariable Long customerId) {
     System.out.println("getOrdersByCustomer is called");
     Optional<Customer> optCustomer = customerRepo.findById(customerId);
     if (!optCustomer.isPresent()) {
         return new ResponseEntity<Collection<SaleorderDTO>>(HttpStatus.NOT_FOUND);
     }
    //  System.out.println(optCustomer.get().getSaleOrders().size());
     Collection<Saleorder> orders = orderRepo.findByCustomerId(customerId);
     List<SaleorderDTO> dtos = new ArrayList<>();
     saleOrderMapper.updateSaleorderFromEntity(orders, dtos);
     return new ResponseEntity<Collection<SaleorderDTO>>(dtos, HttpStatus.OK);
 }

 // GET for getting all orders
 @GetMapping("/orders")
 public ResponseEntity<Collection<SaleorderDTO>> getAllOrders() {
     System.out.println("getAllorder called...");
     Collection<Saleorder> orders = orderRepo.findAll();
     List<SaleorderDTO> dtos = new ArrayList<>();
     saleOrderMapper.updateSaleorderFromEntity(orders, dtos);
     return new ResponseEntity<Collection<SaleorderDTO>>(dtos, HttpStatus.OK);
 }

}
