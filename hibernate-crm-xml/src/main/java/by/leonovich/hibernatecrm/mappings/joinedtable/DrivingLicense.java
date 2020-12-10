package by.leonovich.hibernatecrm.mappings.joinedtable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Random;

/**
 * Created : 06/12/2020 18:22
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DrivingLicense extends Document {
    private DrivingCategory drivingCategory;
    private Boolean international;

    @Override
    public <T extends Document> T populate() {
        super.populate();
        this.setDrivingCategory(DrivingCategory.random());
        this.setInternational(new Random().nextBoolean());
        return (T) this;
    }

    public static DrivingLicense init() {
        DrivingLicense dl = new DrivingLicense();
        return dl.populate();
    }
}