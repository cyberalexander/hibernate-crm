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
        public static final int MAIN_LIMIT = 10;
        public static final int LIMIT = 5;

        public static final String M_PERSIST = "Persisted entity should to be equal to : %s";
        public static final String M_SAVE = "Generated Id must not be null";
        public static final String M_SAVE_CASCADE = "Related entity(ies) should be persisted is database as well.";
        public static final String M_SAVE_OR_UPDATE_SAVE = "Saved entity should be equal to : %s";
        public static final String M_SAVE_OR_UPDATE_UPDATE = "Updated entity should be equal to : %s";
        public static final String M_SAVE_OR_UPDATE_SAVE_CASCADE = "Related Entity(ies) %s expected to be persisted in database along with current entity %s";
        public static final String M_SAVE_OR_UPDATE_UPDATED_CASCADE = "Related Entity(ies) %s expected to be updated in database after %s update operation execution.";
        public static final String M_GET = "Cannot GET %s by Id : %s";
        public static final String M_GET_NOT_EXISTS = "There should not be any record in database for Id : %s";
        public static final String M_LOAD = "Cannot LOAD %s by Id : %s";
        public static final String M_LOAD_EXCEPTION = "Expected org.hibernate.ObjectNotFoundException to be thrown!";
        public static final String M_DELETE ="%s should be deleted from database";
        public static final String M_DELETE_CASCADE_AND_KEEP_RELATION = "%s should not be deleted from database after %s deleted";
        public static final String M_DELETE_CASCADE = "%s should be deleted from database after %s deleted";
        public static final String M_DELETE_ORPHAN = "%s should be deleted from database after detaching from %s";
        public static final String M_GET_ALL = "%s with Id : %s was not found in database.";
        public static final String M_GET_IDS = "%s does not exists in database.";
        public static final String M_MEETING_EXPIRED_TEST = "%s";
        public static final String M_TEST_INDEX = "Column INDEX expected to be equal to [%s]";
        public static final String M_TEST_LAST_INDEX = "Max ID in %s expected to be %s but was %s";
        public static final String M_TEST_SELECT_BY_NAME = "Expected to be selected object(s) with name equal to [%s] but received [%s]";
        public static final String M_TEST_GET_EXPIRING_THIS_YEAR_DOCUMENTS = "Expected to be selected %s objects, but received %s";
        public static final String M_TEST_GET_HIGHEST_PAID_EMPLOYEE = "Expected to be selected %s, but received %s";
}
