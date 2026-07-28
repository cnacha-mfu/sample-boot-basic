package th.mfu;

import java.time.LocalDate;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Books.
 *
 * This controller used to keep books in a static HashMap, which meant every
 * book disappeared when the app stopped. It now uses BookRepository instead,
 * so the data lives in MySQL. Compare the two - the endpoints did not change,
 * only where the data is kept:
 *
 *   bookMap.put(id, book)   ->   bookRepository.save(book)
 *   bookMap.values()        ->   bookRepository.findAll()
 *   bookMap.get(id)         ->   bookRepository.findById(id)
 *   bookMap.remove(id)      ->   bookRepository.deleteById(id)
 *
 * Nobody assigns the id any more: @GeneratedValue(IDENTITY) means MySQL does it,
 * and Hibernate reads it back into the object after the INSERT.
 */
@RestController
@RequestMapping("/api")
public class BookController {

    // @Autowired asks Spring for the repository object it created at startup.
    // You never write "new BookRepository()" - there is no class to instantiate.
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // create new book
    @PostMapping("/books")
    public ResponseEntity<String> createBook(@RequestBody Book book) {
        // The body may name a category as {"category": {"id": 2}}. That object
        // is only a placeholder holding an id, so load the real row before
        // saving - otherwise Hibernate sees an unknown Category and fails.
        ResponseEntity<String> categoryError = attachCategory(book);
        if (categoryError != null) {
            return categoryError;
        }
        if (book.getAddedDate() == null) {
            book.setAddedDate(LocalDate.now());
        }
        Book saved = bookRepository.save(book);
        return new ResponseEntity<String>("Book created with ID: " + saved.getId(), HttpStatus.CREATED);
    }

    // list all books  (GET /api/books)
    // Optional ?author= filter shows a derived query in action.
    @GetMapping("/books")
    public ResponseEntity<List<Book>> listBooks(@RequestParam(required = false) String author) {
        List<Book> books;
        if (author != null) {
            books = bookRepository.findByAuthorContainingIgnoreCase(author);
        } else {
            books = (List<Book>) bookRepository.findAll();
        }
        return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
    }

    // get book by id
    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        // findById returns an Optional, because the row may not exist.
        Optional<Book> book = bookRepository.findById(id);
        if (!book.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(book.get(), HttpStatus.OK);
    }

    // update an existing book
    @PutMapping("/books/{id}")
    public ResponseEntity<String> updateBook(@PathVariable Long id, @RequestBody Book book) {
        Optional<Book> existing = bookRepository.findById(id);
        if (!existing.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ResponseEntity<String> categoryError = attachCategory(book);
        if (categoryError != null) {
            return categoryError;
        }
        // save() on an object that already has an id becomes an UPDATE,
        // not an INSERT. The same method does both.
        book.setId(id);
        if (book.getAddedDate() == null) {
            book.setAddedDate(existing.get().getAddedDate());
        }
        bookRepository.save(book);
        return new ResponseEntity<>("Book with ID: " + id + " updated.", HttpStatus.OK);
    }

    // delete book by id
    @DeleteMapping("/books/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        if (!bookRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        bookRepository.deleteById(id);
        return new ResponseEntity<>("Book with ID: " + id + " deleted.", HttpStatus.OK);
    }

    /**
     * Replaces the placeholder category on an incoming book with the real row
     * from the database. Returns null when everything is fine, or the error
     * response to send back when the named category does not exist.
     */
    private ResponseEntity<String> attachCategory(Book book) {
        if (book.getCategory() == null || book.getCategory().getId() == null) {
            book.setCategory(null);
            return null;
        }
        Long categoryId = book.getCategory().getId();
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (!category.isPresent()) {
            return new ResponseEntity<>("No category with ID: " + categoryId, HttpStatus.BAD_REQUEST);
        }
        book.setCategory(category.get());
        return null;
    }
}
