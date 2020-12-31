package by.leonovich.hibernatecrm.mappings.tableperclass;

import by.leonovich.hibernatecrm.mappings.common.BodyType;
import by.leonovich.hibernatecrm.tools.RandomNumber;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created : 10/12/2020 22:53
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ElectricCar extends Vehicle {
    private Integer batteryCapacity;
    private Integer passengersCount;
    private BodyType bodyType;

    @Override
    public ElectricCar populate() {
        super.populate();
        this.setBatteryCapacity(RandomNumber.BATTERY_CAPACITY.get());
        this.setPassengersCount(RandomNumber.PASSENGERS_COUNT.get());
        this.setBodyType(BodyType.random());
        return this;
    }

    @Override
    public ElectricCar modify() {
        super.modify();
        this.setBatteryCapacity(RandomNumber.BATTERY_CAPACITY.get());
        return this;
    }

    public static ElectricCar init() {
        return new ElectricCar().populate();
    }
}
