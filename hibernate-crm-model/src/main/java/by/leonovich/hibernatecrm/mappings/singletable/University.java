package by.leonovich.hibernatecrm.mappings.singletable;

import by.leonovich.hibernatecrm.common.model.Automated;
import by.leonovich.hibernatecrm.common.random.RandomString;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created : 13/12/2020 19:38
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Data
public class University implements Serializable, Automated<University> {
    private Long id;
    private String name;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Student> students = new HashSet<>(); /* ONE-TO-MANY relation */

    @Override
    public University populate() {
        this.setName(RandomString.DEFAULT.get());
        return this;
    }

    @Override
    public University populateCascade() {
        this.populate();
        this.setStudents(Stream.generate(Student::init).limit(3).collect(Collectors.toSet()));
        this.getStudents().forEach(st -> st.setUniversity(this));
        return this;
    }

    @Override
    public University modify() {
        this.setName(newValue(this.getId(), RandomString.DEFAULT));
        return this;
    }

    @Override
    public University modifyCascade() {
        this.modify();
        this.getStudents().forEach(st -> st.setName(newCascadeValue(this.getId(), RandomString.NAME)));
        return this;
    }

    @Override
    public Serializable incrementIdAndGet() {
        return this.getId() + 500L;
    }

    public static University init() {
        return new University().populate();
    }

    public static University initWithOneToMany() {
        return new University().populateCascade();
    }
}
