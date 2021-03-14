package by.leonovich.hibernatecrm;

import by.leonovich.hibernatecrm.mappings.singletable.Person;
import by.leonovich.hibernatecrm.service.exception.ServiceException;
import by.leonovich.hibernatecrm.service.person.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.stream.Stream;

public class App {
    protected static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("ServiceContext.xml");
        PersonService<Person> personService = context.getBean(PersonService.class);
        Stream.generate(Person::init).limit(3).forEach(p -> {
            try {
                personService.create(p);
            } catch (ServiceException e) {
                throw new RuntimeException(e);
            }
        });
        LOGGER.info("ALL PERSONS : {}", personService.read());
    }
}
