package th.mfu;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A group of books, for example "Programming" or "Fiction".
 *
 * This is the "one" side of the relationship: one category has many books.
 */
@Entity
public class Category {

    // The CATEGORY table uses AUTO_INCREMENT, so the database makes the id.
    // GenerationType.IDENTITY is what tells Hibernate that.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    // One category has many books.
    //
    // mappedBy = "category" says: the Book class owns this link, in its
    // "category" field. Without mappedBy, Hibernate would look for an extra
    // join table (CATEGORY_BOOK) that does not exist, and startup would fail.
    //
    // @JsonIgnore keeps this list out of the JSON. Otherwise a category would
    // print its books, and each book would print its category, forever.
    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Book> books = new ArrayList<>();

    public Category() {
    }

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
