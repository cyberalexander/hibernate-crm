package by.leonovich.hibernatecrm.common;

/**
 * Created : 27/12/2020 11:47
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public record Constants () {
    public static final String UPDATE_PREFIX = "_UPD_";
    public static final String UPDATE_CASCADE_PREFIX = "_UPD_CASCADE_";

    public record Numbers () {
        public static final int THREE = 3;
        public static final int FOUR = 4;
        public static final int FIVE = 5;
        public static final int SIX = 6;
        public static final int EIGHT = 8;
        public static final int TEN = 10;
        public static final int TWELVE = 12;
        public static final int FIFTEEN = 15;
        public static final int ONE_HUNDRED = 100;
        public static final int ONE_HUNDRED_ONE = 100;
        public static final int ONE_THOUSAND = 100;
        public static final int ONE_HUNDRED_THOUSAND = 100_000;
        public static final long TEN_MILLION = 10_000_000L;
        public static final long ONE_BILLION = 1_000_000_000L;
    }
}
