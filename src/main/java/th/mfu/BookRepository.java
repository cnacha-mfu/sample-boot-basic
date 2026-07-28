package th.mfu;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data repository for Book.
 *
 * By extending CrudRepository&lt;Book, Long&gt; you get findAll(), findById(),
 * save(), delete() and count() for free - you write no implementation, and
 * Spring creates the object at startup.
 *
 * The two type parameters are: the entity class, and the type of its @Id field.
 *
 * The methods below are "derived queries": Spring reads the METHOD NAME and
 * writes the SQL for you. findByCategoryId -> WHERE category_id = ?
 */
public interface BookRepository extends CrudRepository<Book, Long> {

    /** All books in one category. Used by GET /api/categories/{id}/books. */
    List<Book> findByCategoryId(Long categoryId);

    /** Exact title match - the same shape as the lab's findByTitle. */
    Book findByTitle(String title);

    /** Case-insensitive "contains" search on the author name. */
    List<Book> findByAuthorContainingIgnoreCase(String author);
}
