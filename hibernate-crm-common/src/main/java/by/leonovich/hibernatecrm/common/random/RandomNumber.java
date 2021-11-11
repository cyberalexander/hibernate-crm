package by.leonovich.hibernatecrm.common.random;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

import static by.leonovich.hibernatecrm.common.Constants.Numbers;

/**
 * Created : 09/12/2020 22:28
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public enum RandomNumber {
    DEFAULT_I(() -> new Random().nextInt(Numbers.ONE_HUNDRED - 1) + 1),
    DEFAULT_L(() -> ThreadLocalRandom.current().nextLong(Numbers.TEN_MILLION, Numbers.ONE_BILLION)),
    DAYS(() -> ThreadLocalRandom.current().nextLong(Numbers.TEN, Numbers.ONE_THOUSAND)),
    MARK(() -> Double.valueOf(String.format("%.1f", 1 + (Numbers.ONE_HUNDRED_ONE - 1) * new Random().nextDouble()))),
    SALARY(() ->
        BigDecimal.valueOf(
            Numbers.ONE_HUNDRED + (Numbers.ONE_HUNDRED_THOUSAND - Numbers.ONE_HUNDRED) * new Random().nextDouble()
        ).setScale(Numbers.FOUR, RoundingMode.CEILING)
    ),
    ENGINE_VOLUME(() -> ThreadLocalRandom.current().nextDouble(Numbers.TEN) * Numbers.FIVE),
    BATTERY_CAPACITY(() -> ThreadLocalRandom.current().nextInt(Numbers.TEN) * Numbers.FIVE),
    PASSENGERS_COUNT(() -> new Random().nextInt(Numbers.EIGHT - 2) + 2);

    private final Supplier<? extends Serializable> supplier;

    RandomNumber(Supplier<? extends Serializable> s) {
        this.supplier = s;
    }

    public <T extends Serializable> T get() {
        return (T) supplier.get();
    }
}
