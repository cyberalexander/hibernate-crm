package by.leonovich.hibernatecrm.mappings.tableperclass;

import by.leonovich.hibernatecrm.common.model.MotorCycleType;
import by.leonovich.hibernatecrm.common.random.RandomNumber;
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
public class MotorCycle extends Vehicle {
    private MotorCycleType type;
    private Double tankCapacity;

    @Override
    public MotorCycle populate() {
        super.populate();
        this.setType(MotorCycleType.random());
        this.setTankCapacity(RandomNumber.ENGINE_VOLUME.get());
        return this;
    }

    @Override
    public MotorCycle modify() {
        super.modify();
        this.setTankCapacity(RandomNumber.ENGINE_VOLUME.get());
        return this;
    }

    public static MotorCycle init() {
        return new MotorCycle().populate();
    }
}
