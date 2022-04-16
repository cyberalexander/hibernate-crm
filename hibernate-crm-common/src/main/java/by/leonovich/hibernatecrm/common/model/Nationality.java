package by.leonovich.hibernatecrm.common.model;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created : 10/12/2020 10:02
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public enum Nationality {
    BELARUSIAN,
    AMERICAN,
    SWISS,
    KOREAN,
    SPANISH,
    SLOVAK,
    RUSSIAN,
    POLISH,
    NORWEGIAN,
    DUTCH;

    private static final List<Nationality> NATIONALITIES = Arrays.asList(Nationality.values());

    public static Nationality random() {
        return NATIONALITIES.get(new Random().nextInt(NATIONALITIES.size() - 1));
    }
}
