package by.leonovich.hibernatecrm.dao;

import by.leonovich.hibernatecrm.mappings.singletable.Employee;
import by.leonovich.hibernatecrm.mappings.singletable.Person;
import by.leonovich.hibernatecrm.mappings.singletable.Student;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created : 07/12/2020 20:41
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class AbstractPersonDaoTest {
    protected static final Logger LOG = LoggerFactory.getLogger(AbstractPersonDaoTest.class);
    protected static final Random r = new Random();
    protected static final Dao<Person> dao = new PersonDao();
    protected static List<Person> allPersons;

    @BeforeAll
    static void beforeAll() {
        Stream<Person> personStream = Stream.generate(Person::init).limit(25);
        Stream<Employee> employeeStream = Stream.generate(Employee::init).limit(30);
        Stream<Student> studentStream = Stream.generate(Student::init).limit(35);

        allPersons = Stream.concat(Stream.concat(personStream, employeeStream), studentStream).collect(Collectors.toList());
        Collections.shuffle(allPersons);
        allPersons.forEach(AbstractPersonDaoTest::persistEach);
    }

    @SneakyThrows
    private static Person persistEach(Person person) {
        dao.saveOrUpdate(person);
        LOG.info("{}", person);
        return person;
    }

    protected int randIndex() {
        return r.nextInt(allPersons.size() - 1);
    }
}
