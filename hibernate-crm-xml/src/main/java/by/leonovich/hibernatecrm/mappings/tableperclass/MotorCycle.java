package by.leonovich.hibernatecrm.mappings.tableperclass;

import by.leonovich.hibernatecrm.mappings.common.MotorCycleType;
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
public class MotorCycle extends Vehicle {
    private MotorCycleType type;
    private Double tankCapacity;

    @Override
    public <T extends Vehicle> T populate() {
        this.setType(MotorCycleType.random());
        this.setTankCapacity(RandomNumber.ENGINE_VOLUME.get());
        return (T) this;
    }

    public static MotorCycle init() {
        return new MotorCycle().populate();
    }
}
