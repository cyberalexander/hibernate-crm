package by.leonovich.hibernatecrm.mappings.annotation;

import by.leonovich.hibernatecrm.common.model.Automated;
import by.leonovich.hibernatecrm.common.model.Nationality;
import by.leonovich.hibernatecrm.common.random.RandomNumber;
import by.leonovich.hibernatecrm.common.random.RandomString;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Random;

/**
 * Created : 06/01/2021 09:54
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Data
@Entity
@Table(name = "T_PORTFOLIO")
public class Portfolio implements Automated {
    @Id
    @Column(name = "F_ID")
    @GeneratedValue
    private Long id;

    @Column(name = "F_BIRTH_DATE")
    private LocalDate birthDate;

    @Column(name = "F_DEATH_DATE", nullable = true)
    private LocalDate deathDate;

    @EqualsAndHashCode.Exclude
    @Formula("YEAR(coalesce(F_DEATH_DATE, sysdate())) - YEAR(F_BIRTH_DATE)")
    private Integer age;

    @Column(name = "F_NATIONALITY")
    @Enumerated(value = EnumType.STRING)
    private Nationality nationality;

    @Column(name = "F_DESCRIPTION")
    private String description;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Cascade(CascadeType.SAVE_UPDATE)
    @OneToOne(mappedBy = "portfolio")
    private Author author; /* ONE-TO-ONE */

    @Override
    public <T> T populate() {
        this.setBirthDate(
            LocalDate.now().minusMonths(RandomNumber.DAYS.get()).minusDays(RandomNumber.DAYS.get())
        );
        this.setDeathDate(
            new Random().nextBoolean() ? this.getBirthDate().plusMonths(RandomNumber.DAYS.get()).plusDays(RandomNumber.DAYS.get()) : null
        );
        this.setNationality(Nationality.random());
        this.setDescription(RandomString.DEFAULT.get().concat(" ").concat(RandomString.DEFAULT.get()));
        return (T) this;
    }

    @Override
    public <T> T populateCascade() {
        populate();
        Author newAuthor = Author.init();
        newAuthor.setPortfolio(this);
        setAuthor(newAuthor);
        return (T) this;
    }

    @Override
    public <T> T modify() {
        this.setNationality(Nationality.random());
        this.setDescription(newValue(this.getId(), RandomString.DEFAULT));
        return (T) this;
    }

    @Override
    public <T> T modifyCascade() {
        getAuthor().setName(newCascadeValue(getId(), RandomString.NAME));
        setDescription(newValue(getId(), RandomString.DEFAULT));
        return (T) this;
    }

    @Override
    public Serializable incrementIdAndGet() {
        return this.getId() + 500L;
    }

    public static Portfolio init() {
        return new Portfolio().populate();
    }

    public static Portfolio initWithOneToOne() {
        return new Portfolio().populateCascade();
    }
}
