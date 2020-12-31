package by.leonovich.hibernatecrm.mappings.joinedtable;

import by.leonovich.hibernatecrm.mappings.common.Nationality;
import by.leonovich.hibernatecrm.tools.RandomString;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created : 06/12/2020 18:18
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Passport extends Document {
    private String passportNumber;
    private Nationality nationality;
    private String issuedBy;

    @Override
    public Passport populate() {
        super.populate();
        this.setPassportNumber(RandomString.PASSPORT_NUMBER.get());
        this.setNationality(Nationality.random());
        this.setIssuedBy(RandomString.ISSUED_BY.get());
        return this;
    }

    @Override
    public Passport modify() {
        super.modify();
        this.setPassportNumber(newValue(this.getId(), RandomString.PASSPORT_NUMBER));
        return this;
    }

    public static Passport init() {
        return new Passport().populate();
    }
}
