package by.leonovich.hibernatecrm.common.random;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created : 13/12/2020 16:09
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public class RandomCountry implements Supplier<Locale> {
    private static final List<Locale> LOCALES = Arrays.stream(Locale.getISOCountries())
        .map(isoCountry -> new Locale("en", isoCountry))
        .collect(Collectors.toList());

    @Override
    public Locale get() {
        return LOCALES.get(new Random().nextInt(LOCALES.size() - 1));
    }
}
