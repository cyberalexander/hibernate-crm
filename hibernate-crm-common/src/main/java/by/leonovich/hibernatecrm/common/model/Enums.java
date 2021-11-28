package by.leonovich.hibernatecrm.common.model;

import java.util.Random;

/**
 * Created : 08/01/2021 19:07
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public final class Enums {

    /**
     * CheckStyle.HideUtilityClassConstructor: Utility classes should not have a public or default constructor.
     */
    private Enums() {
    }

    public static <T extends Enum<T>> T random(final Class<T> enumClass) {
        return random(enumClass.getEnumConstants());
    }

    public static <T> T random(final T... values) {
        return values[new Random().nextInt(values.length)];
    }
}
