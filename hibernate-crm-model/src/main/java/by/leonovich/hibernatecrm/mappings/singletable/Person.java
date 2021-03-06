package by.leonovich.hibernatecrm.mappings.singletable;

import by.leonovich.hibernatecrm.common.model.Automated;
import by.leonovich.hibernatecrm.common.random.RandomNumber;
import by.leonovich.hibernatecrm.common.random.RandomString;
import by.leonovich.hibernatecrm.mappings.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Person extends BaseEntity implements Serializable, Automated {
    private Long id;
    private String name;
    private String surname;
    private Integer age;
    @EqualsAndHashCode.Exclude
    private Address homeAddress; /* COMPONENT relation */
    @EqualsAndHashCode.Exclude
    private PhoneNumber phoneNumber; /* ONE-TO-ONE relation */

    @Override
    public Person populate() {
        this.setName(RandomString.NAME.get());
        this.setSurname(RandomString.SURNAME.get());
        this.setAge(RandomNumber.DEFAULT_I.get());
        this.setHomeAddress(Address.init());
        this.setPhoneNumber(PhoneNumber.init(this));
        return this;
    }

    @Override
    public Person populateCascade() {
        return this.populate();
    }

    @Override
    public Person modify() {
        this.setName(newValue(this.getId(), RandomString.NAME));
        this.getPhoneNumber().setNumber(RandomNumber.DEFAULT_L.get());
        return this;
    }

    @Override
    public Person modifyCascade() {
        return this.modify();
    }

    @Override
    public Serializable incrementIdAndGet() {
        return this.getId() + 500L;
    }

    public static Person init() {
        return new Person().populate();
    }
}
