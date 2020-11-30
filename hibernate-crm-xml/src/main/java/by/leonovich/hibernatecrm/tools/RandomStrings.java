package by.leonovich.hibernatecrm.tools;

import java.util.Random;
import java.util.function.Supplier;

import static java.lang.System.out;

/**
 * Created : 29/11/2020 16:51
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public class RandomStrings implements Supplier<String> {
    private final Random r = new Random();
    private static final char[] CAPITALS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final char[] LOWERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final char[] VOWELS = "aeiou".toCharArray();
    private final int stringLength;

    public static int NAME = new Random().nextInt(8-2) + 2;
    public static int SURNAME = new Random().nextInt(10-3) + 3;
    public static int COMPANY = new Random().nextInt(12-3) + 3;
    public static int FACULTY = new Random().nextInt(10-3) + 3;

    public RandomStrings(int stringLength) {
        this.stringLength = stringLength;
    }

    @Override
    public String get() {
        StringBuilder sb = new StringBuilder();
        sb.append(CAPITALS[r.nextInt(CAPITALS.length)]);
        for (int i = 0; i < stringLength - 1; i++) {
            sb.append(VOWELS[r.nextInt(VOWELS.length)]);
            sb.append(LOWERS[r.nextInt(LOWERS.length)]);
        }
        /*sb.append(" ");*/
        return sb.toString();
    }

    /**
     * Just for testing purpose
     */
    public static void main(String[] args) {
        RandomStrings str = new RandomStrings(3);
        out.println(str.get());
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            out.println("r.nextInt() = " + (r.nextInt(5 - 2) + 2));
        }
    }
}
