package by.leonovich.hibernatecrm.mappings.joinedtable;

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
    private String nationality;
    private String issuedBy;
}
