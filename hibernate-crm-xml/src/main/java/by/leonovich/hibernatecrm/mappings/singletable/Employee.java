package by.leonovich.hibernatecrm.mappings.singletable;

import by.leonovich.hibernatecrm.tools.RandomNumber;
import by.leonovich.hibernatecrm.tools.RandomString;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

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
public class Employee extends Person {

    private String company;
    private BigDecimal salary;

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Person> T populate() {
        super.populate();
        this.setCompany(RandomString.COMPANY.get());
        this.setSalary(RandomNumber.SALARY.get());
        return (T) this;
    }

    public static Employee init() {
        return new Employee().populate();
    }
}
