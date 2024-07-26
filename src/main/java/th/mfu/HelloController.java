package th.mfu;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secure")
public class HelloController {

    @GetMapping("/hello")
    String hello() {
        return "Hello World!";
    }
    String[] validName = {"nacha","john","maria","son"};
    @GetMapping("/hi/{name}")
    ResponseEntity<String> hi(@PathVariable String name){
        //return new ResponseEntity<>("Hi" + name, HttpStatus.OK);
        for(String validName : validName){
            if (validName.equals(name)) {
                return ResponseEntity.ok("Hi " + name);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        //return ResponseEntity.ok("Hi "+name);
    }
   
}
