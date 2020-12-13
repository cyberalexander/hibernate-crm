package by.leonovich.hibernatecrm.mappings.singletable;

import by.leonovich.hibernatecrm.tools.RandomNumber;
import by.leonovich.hibernatecrm.tools.RandomString;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created : 26/11/2020 21:37
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Student extends Person {

    private String faculty;
    private Double mark;

    @Override
    public <T extends Person> T populate() {
        super.populate();
        this.setFaculty(RandomString.FACULTY.get());
        this.setMark(RandomNumber.MARK.get());
        return (T) this;
    }

    public static Student init() {
        return new Student().populate();
    }
}
