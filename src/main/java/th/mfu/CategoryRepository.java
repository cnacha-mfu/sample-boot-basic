package th.mfu;

import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data repository for Category.
 */
public interface CategoryRepository extends CrudRepository<Category, Long> {
}
