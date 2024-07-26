package th.mfu;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import th.mfu.dto.CustomerDTO;

public class CustomerIT {

    private static Client client;

    @BeforeAll
    public static void createClient(){
        client = ClientBuilder.newClient();
    }

    // test for posting a new customer
    @Test
    public void testCreateCustomer(){
        CustomerDTO customer = new CustomerDTO();
        customer.setName("John Denver");
        customer.setAddress("123 Main st.");
        customer.setEmail("john@email.com");
        customer.setPhone("0688888888");

        Builder builder = client.target("http://localhost:8080/customers").request();
        Response response = builder.post(Entity.json(customer));

        assertEquals(201, response.getStatus());
    }

}
