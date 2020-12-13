package by.leonovich.hibernatecrm.tools;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

/**
 * Created : 09/12/2020 22:28
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public enum RandomNumber {
    AGE(() -> new Random().nextInt(99 - 1) + 1),
    DAYS(() -> ThreadLocalRandom.current().nextLong(10, 1000)),
    MARK(() -> Double.valueOf(String.format("%.1f", 1 + (101 - 1) * new Random().nextDouble()))),
    SALARY(() ->
        BigDecimal.valueOf(100 + (100000 - 100) * new Random().nextDouble()).setScale(4, RoundingMode.CEILING)
    ),
    ENGINE_VOLUME(() -> ThreadLocalRandom.current().nextDouble(10) * 5),
    PASSENGERS_COUNT(() -> new Random().nextInt(8 - 2) + 2);

    private final Supplier<? extends Serializable> supplier;

    RandomNumber(Supplier<? extends Serializable> supplier) {
        this.supplier = supplier;
    }

    public <T extends Serializable> T get() {
        return (T) supplier.get();
    }
}
