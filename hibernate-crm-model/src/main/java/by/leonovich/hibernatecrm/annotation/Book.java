package by.leonovich.hibernatecrm.annotation;

import by.leonovich.hibernatecrm.common.model.Automated;
import by.leonovich.hibernatecrm.common.model.Category;
import by.leonovich.hibernatecrm.common.model.Enums;
import by.leonovich.hibernatecrm.common.model.converter.CategoryConverter;
import by.leonovich.hibernatecrm.common.random.RandomNumber;
import by.leonovich.hibernatecrm.common.random.RandomString;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
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
@Table(name = "T_BOOK")
public class Book implements Automated {
    @Id
    @Column(name = "F_ID")
    @GeneratedValue
    private Long id;

    @Column(name = "F_NAME")
    private String name;

    @Column(name = "F_YEAR")
    private Integer year;

    @Column(name = "F_MUST_READ")
    private Boolean mustRead;

    @Column(name = "F_CATEGORY")
    @Convert(converter = CategoryConverter.class)
    private Category category;

    @ManyToOne
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "F_LIBRARY_ID")
    private Library library; /* MANY-TO-ONE */

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "books")
    @Cascade(CascadeType.SAVE_UPDATE)
    private Set<Author> authors = new HashSet<>(); /* MANY-TO-MANY */

    @Override
    public <T> T populate() {
        this.setName(RandomString.NAME.get());
        this.setYear(LocalDate.now().minusYears(RandomNumber.DAYS.get()).getYear());
        this.setMustRead(new Random().nextBoolean());
        this.setCategory(Enums.random(Category.class));
        return (T) this;
    }

    @Override
    public <T> T populateCascade() {
        populate();
        setLibrary(Library.init());
        setAuthors(Stream.generate(Author::init).limit(2).collect(Collectors.toSet()));
        getAuthors().forEach(a -> a.setBooks(Collections.singleton(this)));
        return (T) this;
    }

    @Override
    public <T> T modify() {
        this.setName(newValue(this.getId(), RandomString.NAME));
        this.setYear(LocalDate.now().minusYears(RandomNumber.DAYS.get()).getYear());
        return (T) this;
    }

    @Override
    public <T> T modifyCascade() {
        modify();
        getLibrary().setName(newCascadeValue(getId(), RandomString.COMPANY));
        getAuthors().forEach(author -> author.setName(newCascadeValue(this.getId(), RandomString.NAME)));
        return (T) this;
    }

    @Override
    public Serializable incrementIdAndGet() {
        return this.getId() + 500L;
    }

    public static Book init() {
        return new Book().populate();
    }

    public static Book initCascade() {
        return new Book().populateCascade();
    }
}
