package by.leonovich.hibernatecrm.common.model;

import java.util.Arrays;

/**
 * Created : 07/01/2021 16:13
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public enum Prefix {
    ADM("Adm", "Admiral"),
    ATTY("Atty", "Attorney"),
    BROTHER("Brother", "Brother"),
    CAPTAIN("Capt", "Captain"),
    CHIEF("Chief", "Chief"),
    CMDR("Cmdr", "Commander"),
    DR("Dr", "Doctor"),
    GEN("Gen", "General"),
    MR("Mr", "Mister"),
    MRS("Mrs", "Married Woman"),
    MS("Ms", "Single or Married Woman"),
    PROF("Prof", "Professor"),
    SISTER("Sister", "Sister");

    Prefix(String shortPrefix, String fullPrefix) {
        this.shortPrefix = shortPrefix;
        this.fullPrefix = fullPrefix;
    }

    private final String shortPrefix;
    private final String fullPrefix;

    public String getShortPrefix() {
        return shortPrefix;
    }

    public String getFullPrefix() {
        return fullPrefix;
    }

    public static Prefix of(String prefixValue) {
        return Arrays.stream(Prefix.values())
            .filter(p -> prefixValue.equals(p.getShortPrefix()))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
