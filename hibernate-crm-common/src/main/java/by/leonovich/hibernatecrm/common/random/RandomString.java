package by.leonovich.hibernatecrm.common.random;

import java.util.Random;
import java.util.function.Supplier;

import static by.leonovich.hibernatecrm.common.Constants.Numbers;


/**
 * Created : 09/12/2020 22:02
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public enum RandomString {
    DEFAULT(() -> generate(new Random().nextInt(Numbers.EIGHT - 2) + 2)),
    NAME(() -> {
        int nameLength = new Random().nextInt(Numbers.SIX - 2) + 2;
        return generate(nameLength);
    }),
    SURNAME(() -> {
        int nameLength = new Random().nextInt(Numbers.EIGHT - Numbers.THREE) + Numbers.THREE;
        return generate(nameLength);
    }),
    COMPANY(() -> {
        int nameLength = new Random().nextInt(Numbers.TWELVE - Numbers.THREE) + Numbers.THREE;
        return generate(nameLength);
    }),
    FACULTY(() -> {
        int nameLength = new Random().nextInt(Numbers.TEN - Numbers.THREE) + Numbers.THREE;
        return generate(nameLength);
    }),
    DOCUMENT_NUMBER(() -> generateDocNumber(Numbers.TEN)),
    PASSPORT_NUMBER(() -> generateDocNumber(Numbers.SIX)),
    ISSUED_BY(() -> {
        int issuedByLength = new Random().nextInt(Numbers.FIFTEEN - Numbers.FOUR) + Numbers.FOUR;
        return generate(issuedByLength);
    }),
    MANUFACTURER(() -> {
        int manufacturerLength = new Random().nextInt(Numbers.TEN - 2) + 2;
        return generate(manufacturerLength);
    });

    private final Supplier<String> supplier;
    private static final Random RANDOM = new Random();
    private static final char[] CAPITALS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final char[] LOWERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final char[] VOWELS = "aeiou".toCharArray();
    private static final char[] NUMS = "1234567890".toCharArray();

    RandomString(Supplier<String> s) {
        this.supplier = s;
    }

    public String get() {
        return supplier.get();
    }

    private static String generate(int length) {
        StringBuilder sb = new StringBuilder();
        sb.append(CAPITALS[RANDOM.nextInt(CAPITALS.length)]);
        for (int i = 0; i < length; i++) {
            sb.append(VOWELS[RANDOM.nextInt(VOWELS.length)]);
            sb.append(LOWERS[RANDOM.nextInt(LOWERS.length)]);
        }
        return sb.toString();
    }

    private static String generateDocNumber(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(CAPITALS[RANDOM.nextInt(CAPITALS.length)]);
            sb.append(NUMS[RANDOM.nextInt(NUMS.length)]);
        }
        return sb.toString();
    }
}
