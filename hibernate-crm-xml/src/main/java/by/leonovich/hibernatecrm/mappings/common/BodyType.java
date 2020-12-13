package by.leonovich.hibernatecrm.mappings.common;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created : 13/12/2020 14:33
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public enum BodyType {
    SEDAN,
    COUPE,
    HATCHBACK,
    SPORTS_CAR,
    PICKUP_TRUCK,
    SPORT_UTILITY_VEHICLE,
    CONVERTIBLE;

    private static final List<BodyType> ALL = Arrays.asList(BodyType.values());

    public static BodyType random() {
        return ALL.get(new Random().nextInt(ALL.size() - 1));
    }
}
