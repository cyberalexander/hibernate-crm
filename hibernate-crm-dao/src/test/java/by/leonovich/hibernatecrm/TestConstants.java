package by.leonovich.hibernatecrm;

/**
 * Created : 10/12/2020 19:15
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public class TestConstants {
        public static final int MAIN_LIMIT = 16;
        public static final int LIMIT = 8;

        public static final String UPDATE_PREFIX = "UPD_";
        public static final String UPDATE_CASCADE_PREFIX = "_UPD_CASCADE_";

        public static final String M_PERSIST = "Persisted entity should to be equal to : %s";
        public static final String M_SAVE = "Generated Id must not be null";
        public static final String M_SAVE_OR_UPDATE_SAVE = "Saved entity should be equal to : %s";
        public static final String M_SAVE_OR_UPDATE_UPDATE = "Updated entity should be equal to : %s";
        public static final String M_SAVE_OR_UPDATE_SAVE_CASCADE = "Related Entity(ies) expected to be persisted in database : %s";
        public static final String M_GET = "Cannot GET %s by Id : %s";
        public static final String M_GET_NOT_EXISTS = "There should not be any record in database for Id : %s";
        public static final String M_LOAD = "Cannot LOAD %s by Id : %s";
        public static final String M_LOAD_EXCEPTION = "Expected org.hibernate.ObjectNotFoundException to be thrown!";
        public static final String M_DELETE ="%s should be deleted from database";
        public static final String M_DELETE_RELATION ="%s should not be deleted from database after %s deleted";
        public static final String M_GET_ALL = "%s with Id : %s was not found in database.";
        public static final String M_GET_IDS = "%s does not exists in database.";
        public static final String M_GET_PARTICULAR_TYPE_IDS = "Found %s but only entities of %s type expected to be queried.";
        public static final String M_MEETING_EXPIRED_TEST = "%s";
        public static final String M_TEST_INDEX = "Column INDEX expected to be equal to [%s]";
        public static final String M_EXCEPTION_EXPECTED = "Expected Exception to be thrown at the time of University deletion " +
            "as following Students still has reference to it : %s";
}
