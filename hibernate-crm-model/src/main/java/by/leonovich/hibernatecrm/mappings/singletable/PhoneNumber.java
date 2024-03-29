package by.leonovich.hibernatecrm.mappings.singletable;

import by.leonovich.hibernatecrm.common.model.PhoneNumberType;
import by.leonovich.hibernatecrm.common.random.RandomNumber;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * Created : 13/12/2020 16:54
 * Project : hibernate-crm`
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Data
public class PhoneNumber implements Serializable {
    @Serial
    private static final long serialVersionUID = -3977415971088554604L;
    private Long personId; /* ONE-TO-ONE relation */
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
