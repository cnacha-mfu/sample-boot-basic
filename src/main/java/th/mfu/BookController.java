package th.mfu;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BookController { 

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // create new book
    @PostMapping("/books")
    public ResponseEntity<String> createBook(@RequestBody Book book) {
        //check and attach category if provided
        if (book.getCategory() != null) {
            Optional<Category> category = categoryRepository.findById(book.getCategory().getId());
            if (!category.isPresent()) {
                return new ResponseEntity<>("Category not found with ID: " + book.getCategory().getId(), HttpStatus.BAD_REQUEST);
            }
            book.setCategory(category.get());
        }
        
        Book savedBook = bookRepository.save(book);
        
        return new ResponseEntity<String>("Book created with ID: " + savedBook.getId(), HttpStatus.CREATED);
    }

    // list all books

    @GetMapping("/books")
    public ResponseEntity<Collection> listBooks() {
        List<Book> books = (List<Book>) bookRepository.findAll();
        return new ResponseEntity<Collection>(books, HttpStatus.OK);
    }

    // search books by title
    @GetMapping("/books/title/{title}")
    public ResponseEntity<Collection> searchBooksByTitle(@PathVariable String title) {
        List<Book> books = bookRepository.findByTitleContainingIgnoreCase(title);
        return new ResponseEntity<Collection>(books, HttpStatus.OK);
    }

    // search books by year
    @GetMapping("/books/year/{year}")
    public ResponseEntity<Collection> searchBooksByYear(@PathVariable int year) {
        List<Book> books = bookRepository.findByYear(year);
        return new ResponseEntity<Collection>(books, HttpStatus.OK);
    }

    //get book by id
    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (!book.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Book foundBook = book.get();
        return new ResponseEntity<>(foundBook, HttpStatus.OK);
    }

    // delete book by id
    @DeleteMapping("/books/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (!book.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        bookRepository.deleteById(id);
        return new ResponseEntity<>("Book with ID: " + id + " deleted.", HttpStatus.OK);
    }
}
