package th.mfu;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * A book in the library.
 *
 * This class used to be a plain object kept in a HashMap. Adding @Entity, @Id
 * and @GeneratedValue is the whole change that turns it into a database row.
 *
 * Note that the Jackson annotations (@JsonProperty, @JsonSerialize) and the JPA
 * annotations do different jobs and do not interfere with each other:
 * Jackson controls the JSON, JPA controls the table.
 */
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;

    /** Maps to the column "year", but is called "publish-year" in the JSON. */
    @JsonProperty("publish-year")
    private int year;

    private LocalDate addedDate;

    // Many books belong to one category -> this class holds the FK column
    // category_id. This is the owning side of the relationship.
    @ManyToOne
    private Category category;

    // One book can appear in many transactions. Transaction owns the link,
    // in its "book" field, so this side needs mappedBy.
    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private List<Transaction> transactions = new ArrayList<>();

    /** JPA requires a no-argument constructor to build objects from rows. */
    public Book() {
    }

    public Book(String title, String author, int year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    public LocalDate getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(LocalDate addedDate) {
        this.addedDate = addedDate;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
