package th.mfu;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class CustomerControllerTest {
    //@Autowired
    // private CustomerController controller;

    // @Test
    // public void createAndGet(){
    //     // create a new customer
    //     controller = new CustomerController();
    //     Customer customer = new Customer();
    //     customer.setName("John Denver");
    //     customer.setAddress("123 Main st.");
    //     customer.setEmail("john@email.com");
    //     customer.setPhone("0688888888");

    //     ResponseEntity<String> response = controller.createCustomer(customer);
    //     assertEquals(HttpStatus.CREATED, response.getStatusCode());

    //     // get that customer out
    //     ResponseEntity<Customer> returnCust =  controller.getCustomer(0);
    //     assertEquals(HttpStatus.OK, returnCust.getStatusCode());
    //     assertEquals("John Denver", returnCust.getBody().getName());
    // }

    // @Test
    // public void deleteAndNotFound(){
    //     // create a new customer
    //     controller = new CustomerController();
    //     Customer customer = new Customer();
    //     customer.setName("John Denver");
    //     customer.setAddress("123 Main st.");
    //     customer.setEmail("john@email.com");
    //     customer.setPhone("0688888888");

    //     controller.createCustomer(customer);
    //     // delete that customer
    //     ResponseEntity<String> response = controller.deleteCustomer(0);
    //     assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    //     // get by id, should return 404
    //     ResponseEntity<Customer> response2 = controller.getCustomer(0);
    //     assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
    // }
}
