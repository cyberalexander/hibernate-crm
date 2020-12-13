package by.leonovich.hibernatecrm.mappings.tableperclass;

import by.leonovich.hibernatecrm.mappings.common.BodyType;
import by.leonovich.hibernatecrm.tools.RandomNumber;
import lombok.Data;

/**
 * Created : 10/12/2020 22:53
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Data
public class ElectricCar extends Vehicle {
    private Integer batteryCapacity;
    private Integer passengersCount;
    private BodyType bodyType;

    @Override
    public <T extends Vehicle> T populate() {
        this.setBatteryCapacity(RandomNumber.ENGINE_VOLUME.get());
        this.setPassengersCount(RandomNumber.ENGINE_VOLUME.get());
        this.setBodyType(BodyType.random());
        return (T) this;
    }

    public static ElectricCar init() {
        return new ElectricCar().populate();
    }
}
