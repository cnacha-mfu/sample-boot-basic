package th.mfu;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;

import th.mfu.domain.Customer;
import th.mfu.dto.CustomerDTO;
import th.mfu.dto.mapper.CustomerMapper;
import th.mfu.repository.CustomerRepository;

@RestController
public class CustomerController {
    @Autowired
    private CustomerRepository custRepo;
    @Autowired
    CustomerMapper custMapper;

    //Get for Customer
    @GetMapping("/customer/{id}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable Long id){
        if (!custRepo.existsById(id))
           return new ResponseEntity<CustomerDTO>(HttpStatus.NOT_FOUND);
        
        Optional<Customer> customer = custRepo.findById(id);
        CustomerDTO dto = new CustomerDTO();
        custMapper.updateCustomerFromEntity(customer.get(), dto);
        return new ResponseEntity<CustomerDTO>(dto, HttpStatus.OK);
    }
    @GetMapping("/customers")
    public ResponseEntity<Collection> getAllCustomers(){
        List<Customer> customers = custRepo.findAll();
        List<CustomerDTO> dtos = new ArrayList<CustomerDTO>();
        for(Customer cust: customers){
            CustomerDTO dto = new CustomerDTO();
            custMapper.updateCustomerFromEntity(cust, dto);
            dtos.add(dto);
        }
        return new ResponseEntity<Collection>(dtos,HttpStatus.OK);
    }
    //PATCH
    @PatchMapping("/customers/{id}")
    public ResponseEntity<String> updaCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO){
        if (!custRepo.existsById(id)) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
        Optional<Customer> customerEnt = custRepo.findById(id);
        Customer customer = customerEnt.get();
        custMapper.updateCustomerFromDto(customerDTO,customer);
        custRepo.save(customer);
        return new ResponseEntity<String>("Customer update",HttpStatus.OK);
    }

    //Post for creating a customer
    @PostMapping("/customers")
    public ResponseEntity<String> createCustomer(@RequestBody CustomerDTO customer){
        // if (!custRepo.findByName(customer.getName()).isEmpty()) {
        //     return new ResponseEntity<String>("Customer already use",HttpStatus.CONFLICT);
        // }
        Customer newCust = new Customer();
        custMapper.updateCustomerFromDto(customer, newCust);
        custRepo.save(newCust);
        return new ResponseEntity<String>("Customer created", HttpStatus.CREATED);
    }

    //Delete for deleting a customer by id
    @DeleteMapping("customers/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id){
        custRepo.deleteById(id);
        return new ResponseEntity<String>("Customer deleted", HttpStatus.NO_CONTENT);
    }

}
