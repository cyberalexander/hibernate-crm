package by.leonovich.hibernatecrm.annotation;

import by.leonovich.hibernatecrm.common.model.Automated;
import by.leonovich.hibernatecrm.common.model.Enums;
import by.leonovich.hibernatecrm.common.model.Prefix;
import by.leonovich.hibernatecrm.common.random.RandomString;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

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

    /**
     * F_PORTFOLIO_ID - name of the column in T_AUTHOR table
     * F_ID - name of the Primary Key column in T_PORTFOLIO table
     */
    @OneToOne
    @Cascade(CascadeType.ALL)
    @JoinColumn(name = "F_PORTFOLIO_ID", referencedColumnName = "F_ID")
    private Portfolio portfolio; /* ONE-TO-ONE with foreign key */

    @OneToOne
    @Cascade(CascadeType.ALL)
    @JoinTable(
        name = "T_AUTHOR_TYPEWRITER",
        joinColumns = {@JoinColumn(name = "AUTHOR_ID", referencedColumnName = "ID")},
        inverseJoinColumns = {@JoinColumn(name = "TYPEWRITER_ID", referencedColumnName = "F_ID")}
    )
    private Typewriter typewriter; /* ONE-TO-ONE with a Join Table */

    //TODO implement relation(s)
    //private List<Book> books; /* MANY-TO-MANY */

    @PostLoad
    void fillTransient() {
        if (StringUtils.isNotEmpty(prefixValue)) {
            prefix = Prefix.of(prefixValue);
        }
    }

    @PrePersist
    void fillPersistent() {
        if (Objects.nonNull(prefix)) {
            prefixValue = prefix.getShortPrefix();
        }
    }

    @Override
    public <T> T populate() {
        setPrefix(Enums.random(Prefix.class));
        setName(RandomString.NAME.get());
        setSurname(RandomString.SURNAME.get());
        return (T) this;
    }

    @Override
    public <T> T populateCascade() {
        populate();
        setPortfolio(Portfolio.init());
        setTypewriter(Typewriter.init());
        return (T) this;
    }

    @Override
    public <T> T modify() {
        setName(newValue(getId(), RandomString.NAME));
        setSurname(newValue(getId(), RandomString.SURNAME));
        return (T) this;
    }

    @Override
    public <T> T modifyCascade() {
        setName(newValue(getId(), RandomString.NAME));
        getPortfolio().setDescription(newCascadeValue(getId(), RandomString.DEFAULT));
        getTypewriter().setModel(newCascadeValue(getId(), RandomString.DEFAULT));
        return (T) this;
    }

    @Override
    public Serializable incrementIdAndGet() {
        return getId() + 500L;
    }

    public static Author init() {
        return new Author().populate();
    }

    public static Author initCascade() {
        return new Author().populateCascade();
    }
}
