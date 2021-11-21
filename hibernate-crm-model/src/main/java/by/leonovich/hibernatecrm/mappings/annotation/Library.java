package by.leonovich.hibernatecrm.mappings.annotation;

import by.leonovich.hibernatecrm.common.model.Automated;
import by.leonovich.hibernatecrm.common.random.RandomString;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created : 06/01/2021 09:47
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "T_LIBRARY")
public class Library implements Automated {
    @Id
    @Column(name = "F_ID")
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "F_NAME", nullable = false)
    @EqualsAndHashCode.Include
    private String name;

    @Embedded
    private Location location;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "library", orphanRemoval = true, cascade = {CascadeType.ALL})
    private Set<Book> books; /* ONE-TO-MANY */

    public boolean writeOffBook(Book book) {
        Set<Book> books = new HashSet<>(getBooks());
        boolean result = books.remove(book);
        book.setLibrary(null);
        setBooks(books);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T populate() {
        setName(RandomString.COMPANY.get());
        setLocation(Location.init());
        return (T) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T populateCascade() {
        populate();
        setBooks(Stream.generate(Book::init).limit(2).collect(Collectors.toSet()));
        getBooks().forEach(book -> book.setLibrary(this));
        return (T) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T modify() {
        setName(newValue(getId(), RandomString.COMPANY));
        getLocation().setCity(newCascadeValue(getId(), RandomString.DEFAULT));
        return (T) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T modifyCascade() {
        modify();
        getBooks().forEach(book -> book.setName(newCascadeValue(getId(), RandomString.NAME)));
        return (T) this;
    }

    @Override
    public Serializable incrementIdAndGet() {
        return this.getId() + 500L;
    }

    public static Library init() {
        return new Library().populate();
    }

    public static Library initWitOneToMany() {
        return new Library().populateCascade();
    }
}
