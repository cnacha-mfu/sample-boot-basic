package th.mfu;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * One borrow or one return: a member takes a book, or brings it back.
 *
 * This class sits on the "many" side of TWO relationships, so it holds both
 * foreign keys: book_id and member_id.
 */
@Entity
public class Transaction {

    /** The only two values {@link #type} is allowed to take. */
    public static final String BORROW = "borrow";
    public static final String RETURN = "return";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Either "borrow" or "return". */
    private String type;

    private LocalDate transactionDate;

    // Many transactions can refer to the same book.
    // A @ManyToOne field named "book" maps to the column book_id.
    @ManyToOne
    private Book book;

    // Many transactions can belong to the same member -> column member_id.
    @ManyToOne
    private Member member;

    public Transaction() {
    }

    public Transaction(String type, LocalDate transactionDate, Book book, Member member) {
        this.type = type;
        this.transactionDate = transactionDate;
        this.book = book;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
