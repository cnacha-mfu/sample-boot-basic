package th.mfu;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
public interface BookRepository extends CrudRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByYear(int year);
}
