package by.leonovich.hibernatecrm.mappings.joinedtable;

import java.util.Arrays;
import java.util.List;

/**
 * Created : 06/12/2020 18:23
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public enum DrivingCategory {
    A, B, C, D, E;

    private static final List<DrivingCategory> ALL = Arrays.asList(DrivingCategory.values());

    public static DrivingCategory random() {
        return ALL.get(ALL.size() - 1);
    }
}
