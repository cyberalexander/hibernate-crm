package by.leonovich.hibernatecrm;

import by.leonovich.hibernatecrm.dao.Dao;
import by.leonovich.hibernatecrm.exception.DaoException;
import by.leonovich.hibernatecrm.mappings.singletable.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.Serializable;
import java.util.stream.Stream;

public class App {
    protected static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("DaoContext.xml");
        Dao<Person> personDao = context.getBean("by.leonovich.hibernate.crm.PersonDao", Dao.class);
        Stream.generate(Person::init).limit(3).forEach(p -> {
            try {
                personDao.save(p);
            } catch (DaoException e) {
                throw new RuntimeException(e);
            }
        });
        Serializable lastIndex = personDao.getLastIndex();
        LOGGER.info("INDEX : {}", lastIndex);
    }
}
