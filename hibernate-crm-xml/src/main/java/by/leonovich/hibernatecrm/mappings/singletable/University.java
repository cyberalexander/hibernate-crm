package by.leonovich.hibernatecrm.mappings.singletable;

import by.leonovich.hibernatecrm.tools.RandomString;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created : 13/12/2020 19:38
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Data
public class University implements Serializable {
    private Long id;
    private String universityName;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Student> students = new HashSet<>();

    public University populate() {
        this.setUniversityName(RandomString.DEFAULT.get());
        return this;
    }

    public static University init() {
        return new University().populate();
    }
}
