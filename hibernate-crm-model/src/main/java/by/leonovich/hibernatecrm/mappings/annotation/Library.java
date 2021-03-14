package by.leonovich.hibernatecrm.mappings.annotation;

import by.leonovich.hibernatecrm.common.model.Automated;
import by.leonovich.hibernatecrm.common.random.RandomString;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
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
@Entity
@Table(name = "T_LIBRARY")
public class Library implements Automated {
    @Id
    @Column(name = "F_ID")
    @GeneratedValue
    private Long id;

    @Column(name = "F_NAME", nullable = false)
    private String name;

    @Embedded
    private Location location;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Cascade(CascadeType.ALL)
    @OneToMany(mappedBy = "library", orphanRemoval = true)
    private Set<Book> books; /* ONE-TO-MANY */

    public boolean writeOffBook(Book book) {
        return books.remove(book);
    }

    @Override
    public <T> T populate() {
        setName(RandomString.COMPANY.get());
        setLocation(Location.init());
        return (T) this;
    }

    @Override
    public <T> T populateCascade() {
        populate();
        setBooks(Stream.generate(Book::init).limit(2).collect(Collectors.toSet()));
        getBooks().forEach(book -> book.setLibrary(this));
        return (T) this;
    }

    @Override
    public <T> T modify() {
        setName(newValue(getId(), RandomString.COMPANY));
        getLocation().setCity(newCascadeValue(getId(), RandomString.DEFAULT));
        return (T) this;
    }

    @Override
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
