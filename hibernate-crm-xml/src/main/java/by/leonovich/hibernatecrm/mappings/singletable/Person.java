package by.leonovich.hibernatecrm.mappings.singletable;

import by.leonovich.hibernatecrm.tools.RandomNumber;
import by.leonovich.hibernatecrm.tools.RandomString;
import lombok.Data;

import java.io.Serializable;

/**
 * Created : 26/11/2020 21:28
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Data
public class Person implements Serializable {

    private Long id;
    private String name;
    private String surname;
    private Integer age;

    public <T extends Person> T populate() {
        this.setName(RandomString.NAME.get());
        this.setSurname(RandomString.SURNAME.get());
        this.setAge(RandomNumber.AGE.get());
        return (T) this;
    }

    public static Person init() {
        Person p = new Person();
        return p.populate();
    }
}
