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

    @PostMapping("/students")
    public ResponseEntity<String> registerStudent(@RequestBody Student student) {
        if (students.containsKey(student.getId())) {
            return new ResponseEntity<>("Student id already exists", HttpStatus.CONFLICT);
        }
        students.put(student.getId(), student);
        return new ResponseEntity<>("Student registered successfully", HttpStatus.CREATED);
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable String id) {
        if (!students.containsKey(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(students.get(id), HttpStatus.OK);
    }

    @GetMapping("/students")
    public ResponseEntity<Collection<Student>> listStudents() {
        return new ResponseEntity<>(students.values(), HttpStatus.OK);
    }

}
