package by.leonovich.hibernatecrm;

import by.leonovich.hibernatecrm.dao.Dao;
import by.leonovich.hibernatecrm.dao.PersonDao;
import by.leonovich.hibernatecrm.mappings.singletable.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) throws Exception {
        Dao<Person> personDao = new PersonDao();
        LOGGER.info("{}", personDao.getAll(Person.class));
        LOGGER.info("Hello World!");
    }
}
