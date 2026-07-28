package th.mfu;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data repository for Transaction.
 *
 * Note how a derived query can walk into a related entity: findByMemberId
 * follows the @ManyToOne "member" field down to its id, and becomes
 * WHERE member_id = ?
 */
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    List<Transaction> findByMemberId(Long memberId);

    /** Newest transaction for a book - used to work out if it is on loan. */
    Transaction findFirstByBookIdOrderByIdDesc(Long bookId);
}
