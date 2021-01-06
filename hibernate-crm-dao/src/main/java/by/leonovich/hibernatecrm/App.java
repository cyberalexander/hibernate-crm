package by.leonovich.hibernatecrm;

import by.leonovich.hibernatecrm.dao.Dao;
import by.leonovich.hibernatecrm.dao.PersonDao;
import by.leonovich.hibernatecrm.dao.VehicleDao;
import by.leonovich.hibernatecrm.mappings.singletable.Person;
import by.leonovich.hibernatecrm.mappings.tableperclass.Vehicle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

/**
 * Hello world!
 *
 */
public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) throws Exception {
        Dao<Vehicle> vehicleDao = new VehicleDao();
        Vehicle newVehicle = Vehicle.init();
        vehicleDao.save(newVehicle);
        LOGGER.info("{}", vehicleDao.getAll(Vehicle.class));
        Dao<Person> dao = new PersonDao();
        Person newPerson = Person.init();
        dao.saveOrUpdate(newPerson);
        LOGGER.info("{}", dao.get(newPerson.getId()));

        printLocales();
    }

    @SuppressWarnings("PMD.SystemPrintln")
    private static void printLocales() {
        String[] locales = Locale.getISOCountries();
        for (String countryCode : locales) {
            Locale obj = new Locale("", countryCode);
            System.out.println("obj = " + obj);
            System.out.println("Country Code = " + obj.getCountry()
                + ", Country Name = " + obj.getDisplayCountry());
        }
    }
}
