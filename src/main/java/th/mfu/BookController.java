package th.mfu;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BookController {   

    public static Map<Long, Book> bookMap = new HashMap<>();
    private static long nextId = 1;

    // create new book
    @PostMapping("/books")
    public ResponseEntity<String> createBook(@RequestBody Book book) {
        long id = nextId++;
        book.setId(id);
        bookMap.put(id, book);
        // return ResponseEntity.ok("Book created with ID: " + id);
        return new ResponseEntity<String>("Book created with ID: " + id, HttpStatus.CREATED);
    }

    // list all books
    @GetMapping("/books")
    public ResponseEntity<Collection> listBooks() {
        return new ResponseEntity<Collection>(bookMap.values(), HttpStatus.OK);
    }

    //get book by id
    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        Book book = bookMap.get(id);
        if (book == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    // delete book by id
    @DeleteMapping("/books/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        Book removedBook = bookMap.remove(id);
        if (removedBook == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Book with ID: " + id + " deleted.", HttpStatus.OK);
    }
}
