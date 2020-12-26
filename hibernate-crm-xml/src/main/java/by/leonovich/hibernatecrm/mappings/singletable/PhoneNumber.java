package by.leonovich.hibernatecrm.mappings.singletable;

import by.leonovich.hibernatecrm.mappings.common.PhoneNumberType;
import by.leonovich.hibernatecrm.tools.RandomNumber;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created : 13/12/2020 16:54
 * Project : hibernate-crm`
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Data
public class PhoneNumber {

    private Long personId;
    private PhoneNumberType type;
    private Long number;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Person person;

    @SuppressWarnings("unchecked")
    public <T extends PhoneNumber> T populate(Person p) {
        this.setType(PhoneNumberType.random());
        this.setNumber(RandomNumber.DEFAULT_L.get());
        this.setPerson(p);
        return (T) this;
    }

    public static PhoneNumber init(Person p) {
        return new PhoneNumber().populate(p);
    }
}
