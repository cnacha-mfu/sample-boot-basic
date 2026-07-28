package th.mfu;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Categories, and the books inside them.
 */
@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BookRepository bookRepository;

    // list all categories
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> listCategories() {
        return new ResponseEntity<>((List<Category>) categoryRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (!category.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(category.get(), HttpStatus.OK);
    }

    /**
     * List all books under one category  (GET /api/categories/{id}/books).
     *
     * Two ways to do this, both correct:
     *   1. category.getBooks()                     - follow the @OneToMany
     *   2. bookRepository.findByCategoryId(id)     - a derived query
     *
     * Option 2 is used here because it runs one SELECT with a WHERE clause and
     * needs no open session. Option 1 is shown in the line above it.
     */
    @GetMapping("/categories/{id}/books")
    public ResponseEntity<List<Book>> listBooksInCategory(@PathVariable Long id) {
        if (!categoryRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // List<Book> books = categoryRepository.findById(id).get().getBooks();
        List<Book> books = bookRepository.findByCategoryId(id);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PostMapping("/categories")
    public ResponseEntity<String> createCategory(@RequestBody Category category) {
        Category saved = categoryRepository.save(category);
        return new ResponseEntity<>("Category created with ID: " + saved.getId(), HttpStatus.CREATED);
    }
}
