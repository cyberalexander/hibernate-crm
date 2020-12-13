package by.leonovich.hibernatecrm.mappings.common;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
        return ALL.get(new Random().nextInt(ALL.size() - 1));
    }
}
