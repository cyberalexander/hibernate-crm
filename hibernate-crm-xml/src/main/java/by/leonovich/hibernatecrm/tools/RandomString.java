package by.leonovich.hibernatecrm.tools;

import java.util.Random;
import java.util.function.Supplier;

/**
 * Created : 09/12/2020 22:02
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public enum RandomString {
    DEFAULT(() -> generate(new Random().nextInt(8 - 2) + 2)),
    NAME(() -> {
        int nameLength = new Random().nextInt(6 - 2) + 2;
        return generate(nameLength);
    }),
    SURNAME(() -> {
        int nameLength = new Random().nextInt(8 - 3) + 3;
        return generate(nameLength);
    }),
    COMPANY(() -> {
        int nameLength = new Random().nextInt(12-3) + 3;
        return generate(nameLength);
    }),
    FACULTY(() -> {
        int nameLength = new Random().nextInt(10-3) + 3;
        return generate(nameLength);
    }),
    DOCUMENT_NUMBER(() -> generateDocNumber(10)),
    PASSPORT_NUMBER(() -> generateDocNumber(6)),
    ISSUED_BY(() -> {
        int issuedByLength = new Random().nextInt(15-4) + 4;
        return generate(issuedByLength);
    }),
    MANUFACTURER(() -> {
        int manufacturerLength = new Random().nextInt(10-2) + 2;
        return generate(manufacturerLength);
    });

    private final Supplier<String> supplier;
    private static final Random r = new Random();
    private static final char[] CAPITALS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final char[] LOWERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final char[] VOWELS = "aeiou".toCharArray();
    private static final char[] NUMS = "1234567890".toCharArray();

    RandomString(Supplier<String> supplier) {
        this.supplier = supplier;
    }

    public String get() {
        return supplier.get();
    }

    private static String generate(int length) {
        StringBuilder sb = new StringBuilder();
        sb.append(CAPITALS[r.nextInt(CAPITALS.length)]);
        for (int i = 0; i < length; i++) {
            sb.append(VOWELS[r.nextInt(VOWELS.length)]);
            sb.append(LOWERS[r.nextInt(LOWERS.length)]);
        }
        return sb.toString();
    }

    private static String generateDocNumber(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(CAPITALS[r.nextInt(CAPITALS.length)]);
            sb.append(NUMS[r.nextInt(NUMS.length)]);
        }
        return sb.toString();
    }
}
