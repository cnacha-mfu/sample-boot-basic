package th.mfu;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController {

    // the "database" for today: key = student id, value = the student
    public static Map<String, Student> students = new HashMap<String, Student>();

    // TODO: add @PostMapping("/students") and @RequestBody on the parameter
    public ResponseEntity<String> registerStudent(Student student) {
        // TODO: if the id already exists in the map, return 409 CONFLICT
        // TODO: otherwise put the student in the map and return 201 CREATED
        return null;
    }

    // TODO: add @GetMapping("/students/{id}") and @PathVariable on the parameter
    public ResponseEntity<Student> getStudent(String id) {
        // TODO: if the id is not in the map, return 404 NOT FOUND
        // TODO: otherwise return the student with 200 OK
        return null;
    }

    // TODO: add @GetMapping("/students")
    public ResponseEntity<Collection<Student>> listStudents() {
        // TODO: return all values in the map with 200 OK
        return null;
    }

}
