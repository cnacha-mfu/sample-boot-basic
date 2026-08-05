package th.mfu;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// This test is already complete — read it together, then make it pass.
// It calls the controller like a plain Java object: no server, no browser.
// The lab's UserControllerTest grades you exactly this way.
public class StudentControllerTest {

    private StudentController controller = new StudentController();

    @BeforeEach
    public void resetStore() {
        StudentController.students.clear();
    }

    @Test
    public void testRegisterAndGetStudent() {
        Student s = new Student("6531501001", "Alice", "alice@lamduan.mfu.ac.th");

        ResponseEntity<String> response = controller.registerStudent(s);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        ResponseEntity<Student> fetched = controller.getStudent("6531501001");
        assertEquals(HttpStatus.OK, fetched.getStatusCode());
        assertEquals("Alice", fetched.getBody().getName());
    }

    @Test
    public void testRegisterDuplicateStudent() {
        Student s1 = new Student("6531501001", "Alice", "alice@lamduan.mfu.ac.th");
        Student s2 = new Student("6531501001", "Alice Again", "alice2@lamduan.mfu.ac.th");

        assertEquals(HttpStatus.CREATED, controller.registerStudent(s1).getStatusCode());
        assertEquals(HttpStatus.CONFLICT, controller.registerStudent(s2).getStatusCode());
    }

    @Test
    public void testGetUnknownStudent() {
        ResponseEntity<Student> response = controller.getStudent("nobody");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testListStudents() {
        controller.registerStudent(new Student("6531501001", "Alice", "alice@lamduan.mfu.ac.th"));
        controller.registerStudent(new Student("6531501002", "Bob", "bob@lamduan.mfu.ac.th"));

        ResponseEntity<java.util.Collection<Student>> response = controller.listStudents();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }
}
