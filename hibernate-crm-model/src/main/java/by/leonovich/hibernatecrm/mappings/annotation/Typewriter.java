package by.leonovich.hibernatecrm.mappings.annotation;

import by.leonovich.hibernatecrm.common.model.Automated;
import by.leonovich.hibernatecrm.common.random.RandomNumber;
import by.leonovich.hibernatecrm.common.random.RandomString;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created : 13/01/2021 16:16
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Data
@Entity
@Table(name = "T_TYPEWRITER")
public class Typewriter implements Automated {

    @Id
    @Column(name = "F_ID")
    @GeneratedValue
    private Long id;

    @Column(name = "F_MODEL")
    private String model;

    @Column(name = "F_SERIAL_NUMBER")
    private Long serialNumber;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Cascade(CascadeType.SAVE_UPDATE)
    @OneToOne(mappedBy = "typewriter")
    private Author author;  /* ONE-TO-ONE with a Join Table */

    @Override
    public <T> T populate() {
        setModel(RandomString.DEFAULT.get());
        setSerialNumber(RandomNumber.DEFAULT_L.get());
        return (T) this;
    }

    @Override
    public <T> T populateCascade() {
        populate();
        Author author = Author.init();
        author.setTypewriter(this);
        setAuthor(author);
        return (T) this;
    }

    @Override
    public <T> T modify() {
        setModel(newValue(getId(), RandomString.DEFAULT));
        setSerialNumber(RandomNumber.DEFAULT_L.get());
        return (T) this;
    }

    @Override
    public <T> T modifyCascade() {
        setModel(newValue(getId(), RandomString.DEFAULT));
        getAuthor().setName(newCascadeValue(getId(), RandomString.NAME));
        return (T) this;
    }

    @Override
    public Serializable incrementIdAndGet() {
        return this.getId() + 500L;
    }

    public static Typewriter init() {
        return new Typewriter().populate();
    }

    public static Typewriter initWithOneToOne() {
        return new Typewriter().populateCascade();
    }
}
