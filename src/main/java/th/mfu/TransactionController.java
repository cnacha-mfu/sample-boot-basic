package th.mfu;

import java.time.LocalDate;
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
 * Borrowing and returning books.
 *
 * All four transaction routes live here, including the two under /members/{id},
 * so the borrow/return checks are written once instead of being copied into
 * MemberController.
 */
@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> listTransactions() {
        return new ResponseEntity<>((List<Transaction>) transactionRepository.findAll(), HttpStatus.OK);
    }

    /** The borrowing history of one member. */
    @GetMapping("/members/{id}/transactions")
    public ResponseEntity<List<Transaction>> listMemberTransactions(@PathVariable Long id) {
        if (!memberRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(transactionRepository.findByMemberId(id), HttpStatus.OK);
    }

    /**
     * Borrow or return a book  (POST /api/transactions).
     *
     * Body: { "book": {"id": 3}, "member": {"id": 2}, "type": "borrow" }
     *
     * The body is turned straight into a Transaction object. The nested book and
     * member only carry an id - the same style BookController uses for category.
     */
    @PostMapping("/transactions")
    public ResponseEntity<?> createTransaction(@RequestBody Transaction transaction) {
        Long memberId = (transaction.getMember() == null) ? null : transaction.getMember().getId();
        return record(transaction, memberId);
    }

    /**
     * Borrow or return a book  (POST /api/members/{id}/transactions).
     *
     * Body: { "book": {"id": 3}, "type": "borrow" }
     * The member comes from the URL, so any member in the body is ignored.
     */
    @PostMapping("/members/{id}/transactions")
    public ResponseEntity<?> createMemberTransaction(@PathVariable Long id,
            @RequestBody Transaction transaction) {
        return record(transaction, id);
    }

    /**
     * The borrow / return rules, shared by both POST endpoints.
     *
     * The incoming transaction holds placeholder Book and Member objects that
     * only have an id in them, so the real rows are loaded and put in their
     * place before saving.
     */
    private ResponseEntity<?> record(Transaction transaction, Long memberId) {
        Long bookId = (transaction.getBook() == null) ? null : transaction.getBook().getId();
        String type = transaction.getType();

        if (bookId == null || memberId == null) {
            return new ResponseEntity<>("book id and member id are required", HttpStatus.BAD_REQUEST);
        }
        if (!Transaction.BORROW.equals(type) && !Transaction.RETURN.equals(type)) {
            return new ResponseEntity<>("type must be 'borrow' or 'return'", HttpStatus.BAD_REQUEST);
        }

        Optional<Book> book = bookRepository.findById(bookId);
        if (!book.isPresent()) {
            return new ResponseEntity<>("No book with ID: " + bookId, HttpStatus.NOT_FOUND);
        }
        Optional<Member> member = memberRepository.findById(memberId);
        if (!member.isPresent()) {
            return new ResponseEntity<>("No member with ID: " + memberId, HttpStatus.NOT_FOUND);
        }

        // The newest transaction for this book tells us whether it is on loan.
        Transaction latest = transactionRepository.findFirstByBookIdOrderByIdDesc(bookId);
        boolean onLoan = latest != null && Transaction.BORROW.equals(latest.getType());

        if (Transaction.BORROW.equals(type) && onLoan) {
            return new ResponseEntity<>("Book " + bookId + " is already borrowed", HttpStatus.BAD_REQUEST);
        }
        if (Transaction.RETURN.equals(type)) {
            if (!onLoan) {
                return new ResponseEntity<>("Book " + bookId + " is not currently borrowed",
                        HttpStatus.BAD_REQUEST);
            }
            if (!latest.getMember().getId().equals(memberId)) {
                return new ResponseEntity<>("Book " + bookId + " was borrowed by member "
                        + latest.getMember().getId() + ", not by member " + memberId,
                        HttpStatus.BAD_REQUEST);
            }
        }

        // Swap the placeholders for the real rows, then save.
        transaction.setId(null);
        transaction.setBook(book.get());
        transaction.setMember(member.get());
        transaction.setTransactionDate(LocalDate.now());

        Transaction saved = transactionRepository.save(transaction);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
}
