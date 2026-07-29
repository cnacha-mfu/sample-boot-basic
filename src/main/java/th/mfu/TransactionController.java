package th.mfu;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BookRepository bookRepository;

    // record transaction
    @PostMapping("/transactions")
    public ResponseEntity<String> recordTransaction(@RequestBody Transaction transaction) {
        // Validate member
        Optional<Member> member = memberRepository.findById(transaction.getMember().getId());
        if (!member.isPresent()) {
            return new ResponseEntity<>("Member not found with ID: " + transaction.getMember().getId(), HttpStatus.BAD_REQUEST);
        }
        // Validate book
        Optional<Book> book = bookRepository.findById(transaction.getBook().getId());
        if (!book.isPresent()) {
            return new ResponseEntity<>("Book not found with ID: " + transaction.getBook().getId(), HttpStatus.BAD_REQUEST);
        }

        // Save transaction
        transaction.setMember(member.get());
        transaction.setBook(book.get());
        transaction.setTransactionDate(java.time.LocalDate.now());
        Transaction savedTransaction = transactionRepository.save(transaction);
        return new ResponseEntity<>("Transaction recorded with ID: " + savedTransaction.getId(), HttpStatus.CREATED);
    }

    //list all transactions
    @GetMapping("/transactions")
    public ResponseEntity<Iterable<Transaction>> listTransactions() {
        Iterable<Transaction> transactions = transactionRepository.findAll();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

}
