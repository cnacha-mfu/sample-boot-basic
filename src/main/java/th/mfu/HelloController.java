package th.mfu;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/hello")
    String hello() {
        return "Hello World!";
    }

    @GetMapping("/hello/{name}")
    String helloName(@PathVariable String name) {
        // TODO: return a greeting that includes the name from the URL
        return "";
    }

}
