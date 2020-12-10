package by.leonovich.hibernatecrm.mappings.joinedtable;

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
    public <T extends Document> T populate() {
        super.populate();
        this.setPassportNumber(RandomString.PASSPORT_NUMBER.get());
        this.setNationality(Nationality.random());
        this.setIssuedBy(RandomString.ISSUED_BY.get());
        return (T) this;
    }

    public static Passport init() {
        Passport p = new Passport();
        return p.populate();
    }
}
