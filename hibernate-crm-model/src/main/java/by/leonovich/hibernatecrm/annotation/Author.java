package by.leonovich.hibernatecrm.annotation;

import by.leonovich.hibernatecrm.common.model.Automated;
import by.leonovich.hibernatecrm.common.model.Enums;
import by.leonovich.hibernatecrm.common.model.Prefix;
import by.leonovich.hibernatecrm.common.random.RandomString;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

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
@Table(name = "T_AUTHOR")
@SequenceGenerator(name = "AUTHOR_PK", sequenceName = "T_AUTHOR_SEQ")
public class Author implements Automated {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTHOR_PK")
    private Long id;

    /**
     * {@link Prefix#getShortPrefix()} will be stored in database
     */
    @Column(name = "F_PREFIX")
    private String prefixValue;

    /**
     * {@link Prefix} will be used in code
     */
    @Transient
    private Prefix prefix;

    @Column(name = "F_NAME")
    private String name;

    @Column(name = "F_SURNAME")
    private String surname;

    //TODO implement relation(s)
    //private Portfolio portfolio; /* ONE-TO-ONE */
    //private List<Book> books; /* MANY-TO-MANY */

    @PostLoad
    void fillTransient() {
        if (StringUtils.isNotEmpty(this.prefixValue)) {
            this.prefix = Prefix.of(this.prefixValue);
        }
    }

    @PrePersist
    void fillPersistent() {
        if (Objects.nonNull(this.prefix)) {
            this.prefixValue = this.prefix.getShortPrefix();
        }
    }

    @Override
    public <T> T populate() {
        this.setPrefix(Enums.random(Prefix.class));
        this.setName(RandomString.NAME.get());
        this.setSurname(RandomString.SURNAME.get());
        return (T) this;
    }

    @Override
    public <T> T modify() {
        this.setName(newValue(this.getId(), RandomString.NAME));
        this.setSurname(newValue(this.getId(), RandomString.SURNAME));
        return (T) this;
    }

    @Override
    public Serializable incrementIdAndGet() {
        return this.getId() + 500L;
    }

    public static Author init() {
        return new Author().populate();
    }
}
