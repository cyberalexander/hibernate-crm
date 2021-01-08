package by.leonovich.hibernatecrm.annotation;

import by.leonovich.hibernatecrm.common.model.Automated;
import by.leonovich.hibernatecrm.common.random.RandomString;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

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

    //TODO implement relation(s)
    //private Location location;
    //private List<Book> books; /* ONE-TO-MANY */

    @Override
    public <T> T populate() {
        this.setName(RandomString.COMPANY.get());
        return (T) this;
    }

    @Override
    public <T> T modify() {
        this.setName(newValue(this.getId(), RandomString.COMPANY));
        return (T) this;
    }

    @Override
    public Serializable incrementIdAndGet() {
        return this.getId() + 500L;
    }

    public static Library init() {
        return new Library().populate();
    }
}
