package by.leonovich.hibernatecrm;

import by.leonovich.hibernatecrm.exception.HibernateCrmApplicationException;
import by.leonovich.hibernatecrm.mappings.singletable.Person;
import by.leonovich.hibernatecrm.service.exception.ServiceException;
import by.leonovich.hibernatecrm.service.person.PersonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.stream.Stream;

public class App {
    protected static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) throws Exception {
        var context = new AnnotationConfigApplicationContext(ServiceConfiguration.class);
        var personService = context.getBean("personServiceImpl", PersonServiceImpl.class);
        Stream.generate(Person::init).limit(3).forEach(p -> {
            try {
                personService.create(p);
            } catch (ServiceException e) {
                throw new HibernateCrmApplicationException(e);
            }
        });

        personService.read().forEach(p -> LOGGER.info("{}", p));
    }
}
