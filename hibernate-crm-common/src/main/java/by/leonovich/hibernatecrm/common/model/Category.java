package by.leonovich.hibernatecrm.common.model;

/**
 * Created : 08/01/2021 19:02
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public enum Category {
    CLASSIC("CC"),
    ACTION("AN"),
    ADVENTURE("AE"),
    COMIC_BOOK("CK"),
    NOVEL("NL"),
    DETECTIVE("DE"),
    MYSTERY("MY"),
    FANTASY("FY"),
    HISTORY("HY");

    private final String categoryCode;

    Category(String code) {
        this.categoryCode = code;
    }

    public String getCategoryCode() {
        return categoryCode;
    }
}
