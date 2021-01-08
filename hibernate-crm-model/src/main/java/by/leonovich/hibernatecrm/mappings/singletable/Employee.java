package by.leonovich.hibernatecrm.mappings.singletable;

import by.leonovich.hibernatecrm.common.model.Automated;
import by.leonovich.hibernatecrm.common.random.RandomNumber;
import by.leonovich.hibernatecrm.common.random.RandomString;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created : 26/11/2020 21:36
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Employee extends Person implements Automated {
    private String company;
    private BigDecimal salary;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Meeting> meetings = new HashSet<>(); /* MANY-TO-MANY relation */

    @Override
    public Employee populate() {
        super.populate();
        this.setCompany(RandomString.COMPANY.get());
        this.setSalary(RandomNumber.SALARY.get());
        return this;
    }

    @Override
    public Employee populateCascade() {
        this.populate();
        this.setMeetings(Stream.generate(Meeting::init).limit(2).collect(Collectors.toSet()));
        this.getMeetings().forEach(m -> m.getEmployees().add(this));
        return this;
    }

    @Override
    public Employee modify() {
        super.modify();
        this.setCompany(newValue(this.getId(), RandomString.COMPANY));
        return this;
    }

    @Override
    public Employee modifyCascade() {
        this.modify();
        this.getMeetings().forEach(m -> m.setSubject(newCascadeValue(this.getId(), RandomString.DEFAULT)));
        return this;
    }

    public static Employee init() {
        return new Employee().populate();
    }

    public static Employee initWithManyToMany() {
        return new Employee().populateCascade();
    }
}
