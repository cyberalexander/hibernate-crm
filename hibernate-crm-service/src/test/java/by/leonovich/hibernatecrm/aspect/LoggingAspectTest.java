package by.leonovich.hibernatecrm.aspect;

import by.leonovich.hibernatecrm.ServiceConfiguration;
import by.leonovich.hibernatecrm.mappings.singletable.Person;
import by.leonovich.hibernatecrm.service.person.PersonService;
import lombok.SneakyThrows;
import nl.altindag.log.LogCaptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Created : 13/03/2021 21:42
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ServiceConfiguration.class})
class LoggingAspectTest {
    private static final Logger LOG = LogManager.getLogger(LoggingAspectTest.class);

    @Autowired
    private PersonService<Person> personService;

    @Test
    @SneakyThrows
    void testAroundAspect() {
        LogCaptor captor = LogCaptor.forClass(LoggingAspect.class);
        Person person = Person.init();
        personService.create(person);
        LOG.info("s : {}", captor);
        MatcherAssert.assertThat(
            "",
            captor.getWarnLogs().stream().anyMatch(log -> log.startsWith("Exec time : ")),
            Matchers.is(Boolean.TRUE)
        );
    }
}
