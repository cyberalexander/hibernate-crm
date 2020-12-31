package by.leonovich.hibernatecrm.mappings.joinedtable;

import by.leonovich.hibernatecrm.mappings.common.DrivingCategory;
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
    public DrivingLicense populate() {
        super.populate();
        this.setDrivingCategory(DrivingCategory.random());
        this.setInternational(new Random().nextBoolean());
        return this;
    }

    @Override
    public DrivingLicense modify() {
        super.modify();
        this.setInternational(new Random().nextBoolean());
        return this;
    }

    public static DrivingLicense init() {
        return new DrivingLicense().populate();
    }
}
