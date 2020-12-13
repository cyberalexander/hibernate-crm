package by.leonovich.hibernatecrm.mappings.common;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created : 13/12/2020 14:29
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public enum MotorCycleType {
    TOURISTIC,
    SPORT,
    CHOPPER,
    CRUISER,
    DRAGSTER;

    private static final List<MotorCycleType> ALL = Arrays.asList(MotorCycleType.values());

    public static MotorCycleType random() {
        return ALL.get(new Random().nextInt(ALL.size() - 1));
    }
}
