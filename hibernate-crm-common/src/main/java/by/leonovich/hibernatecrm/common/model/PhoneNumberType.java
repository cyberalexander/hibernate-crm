package by.leonovich.hibernatecrm.common.model;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created : 13/12/2020 16:55
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public enum PhoneNumberType {
    HOME,
    WORK,
    MOBILE,
    PRIVATE;

    private static final List<PhoneNumberType> ALL = Arrays.asList(PhoneNumberType.values());

    public static PhoneNumberType random() {
        return ALL.get(new Random().nextInt(ALL.size() - 1));
    }
}
