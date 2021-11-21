package by.leonovich.hibernatecrm.mappings.annotation;

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
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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
@Table(name = "T_BOOK")
public class Book implements Automated {
    @Id
    @Column(name = "F_ID")
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "F_NAME")
    @EqualsAndHashCode.Include
    private String name;

    @Column(name = "F_YEAR")
    private Integer year;

    @Column(name = "F_MUST_READ")
    private Boolean mustRead;

    @Column(name = "F_CATEGORY")
    @Convert(converter = CategoryConverter.class)
    private Category category;

    @ManyToOne(targetEntity = Library.class)
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "F_LIBRARY_ID")
    private Library library; /* MANY-TO-ONE */

    @ToString.Exclude
    @ManyToMany(mappedBy = "books")
    @Cascade(CascadeType.SAVE_UPDATE)
    private Set<Author> authors = new HashSet<>(); /* MANY-TO-MANY */

    /**
     * TODO: remove this temp solution
     */
    public Set<Author> getAuthors() {
        return new HashSet<>(authors);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T populate() {
        this.setName(RandomString.NAME.get());
        this.setYear(LocalDate.now().minusYears(RandomNumber.DAYS.get()).getYear());
        this.setMustRead(new Random().nextBoolean());
        this.setCategory(Enums.random(Category.class));
        return (T) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T populateCascade() {
        populate();
        setLibrary(Library.init());
        setAuthors(Set.of(Author.init(), Author.init(), Author.init()));
        getAuthors().forEach(a -> a.setBooks(Set.of(this)));
        return (T) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T modify() {
        this.setName(newValue(this.getId(), RandomString.NAME));
        this.setYear(LocalDate.now().minusYears(RandomNumber.DAYS.get()).getYear());
        return (T) this;
    }

    @Override
    @SuppressWarnings("unchecked")
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
