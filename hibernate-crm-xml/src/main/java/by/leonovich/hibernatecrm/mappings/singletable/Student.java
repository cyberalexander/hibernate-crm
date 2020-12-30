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

    private University university; /* MANY-TO-ONE relation */
    private String faculty;
    private Double mark;

    @Override
    public Student populate() {
        super.populate();
        this.setFaculty(RandomString.FACULTY.get());
        this.setMark(RandomNumber.MARK.get());
        return this;
    }

    @Override
    public Student populateCascade() {
        this.populate();
        this.setUniversity(University.init());
        this.getUniversity().getStudents().add(this);
        return this;
    }

    @Override
    public Student modify() {
        super.modify();
        this.setFaculty(newValue(this.getId(), RandomString.FACULTY));
        return this;
    }

    @Override
    public Student modifyCascade() {
        this.modify();
        this.getUniversity().setName(newCascadeValue(this.getId(), RandomString.DEFAULT));
        return this;
    }

    public static Student init() {
        return new Student().populate();
    }

    public static Student initWithManyToOne() {
        return new Student().populateCascade();
    }
}
