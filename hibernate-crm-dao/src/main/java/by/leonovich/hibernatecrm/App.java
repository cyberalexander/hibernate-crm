package by.leonovich.hibernatecrm;

import by.leonovich.hibernatecrm.dao.Dao;
import by.leonovich.hibernatecrm.dao.PersonDao;
import by.leonovich.hibernatecrm.mappings.singletable.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

/**
 * Hello world!
 *
 */
public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    @SuppressWarnings("PMD.SystemPrintln")
    public static void main( String[] args ) throws Exception {
        Dao<Person> dao = new PersonDao();
        LOGGER.info("{}", dao.getAll(Person.class));
        LOGGER.info("Hello World!");

        String[] locales = Locale.getISOCountries();
        for (String countryCode : locales) {
            Locale obj = new Locale("", countryCode);
            System.out.println("obj = " + obj);
            System.out.println("Country Code = " + obj.getCountry()
                + ", Country Name = " + obj.getDisplayCountry());
        }
    }
}
