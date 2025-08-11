package th.mfu;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;

@RestController
public class CustomerController {

    @Autowired
    private CustomerRepository custRepo;

    @Autowired
    private SaleOrderRepository orderRepo;

    @Autowired
    private CustomerTierRepository tierRepo;

    // GET for a customer
    @GetMapping("/customers/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long id){
        if(!custRepo.existsById(id))
            return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
        Optional<Customer> customer = custRepo.findById(id);
        return new ResponseEntity<Customer>(customer.get(), HttpStatus.OK);
    }

    // GET all orders for a specific customer
    @GetMapping("/customers/{id}/orders")
    public ResponseEntity<List<SaleOrder>> getOrdersForCustomer(@PathVariable Long id) {
        if (!custRepo.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<SaleOrder> orders = orderRepo.findByCustomerId(id);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // Get all customer
    @GetMapping("/customers")
    public ResponseEntity<Collection> getAllCustomers(){
        return new ResponseEntity<Collection>(custRepo.findAll(), HttpStatus.OK);
    }

    @GetMapping("/customers/name/{prefix}")
    public ResponseEntity<Collection> searchCustomerByName(@PathVariable String prefix){
        List<Customer> results = custRepo.findByNameStartingWith(prefix);
        return new ResponseEntity<Collection>(results, HttpStatus.OK);
    }


    // POST for creating a customer
    @PostMapping("/customers")
    public ResponseEntity<String> createCustomer(@RequestBody Customer customer){
        // Handle CustomerTier association
        if (customer.getCustomerTier() != null && customer.getCustomerTier().getId() != null) {
            Optional<CustomerTier> tierOpt = tierRepo.findById(customer.getCustomerTier().getId());
            if (tierOpt.isPresent()) {
                customer.setCustomerTier(tierOpt.get());
            } else {
                return new ResponseEntity<>("CustomerTier with given ID not found", HttpStatus.BAD_REQUEST);
            }
        }
        custRepo.save(customer);
        return new ResponseEntity<>("Customer created with ID " + customer.getId(), HttpStatus.CREATED);
    }

    // DELETE for deleting a customer by id
    @DeleteMapping("customers/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id){
        custRepo.deleteById(id);
        return new ResponseEntity<String>("Customer deleted", HttpStatus.NO_CONTENT);
    }

    // PUT to update a customer's tier
    @PutMapping("/customers/{id}/tier")
    public ResponseEntity<Customer> updateCustomerTier(@PathVariable Long id, @RequestBody Customer customerPayload) {
        // 1. Validate and get tierId from payload
        if (customerPayload.getCustomerTier() == null || customerPayload.getCustomerTier().getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        Long tierId = customerPayload.getCustomerTier().getId();

        // 2. Find the customer
        Optional<Customer> customerOpt = custRepo.findById(id);
        if (customerOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // 3. Find the new tier
        Optional<CustomerTier> tierOpt = tierRepo.findById(tierId);
        if (tierOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        // 4. Update and save the customer
        Customer customerToUpdate = customerOpt.get();
        customerToUpdate.setCustomerTier(tierOpt.get());
        final Customer updatedCustomer = custRepo.save(customerToUpdate);
        return ResponseEntity.ok(updatedCustomer);
    }
}
