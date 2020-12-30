package by.leonovich.hibernatecrm.dao.person;

import by.leonovich.hibernatecrm.dao.Dao;
import by.leonovich.hibernatecrm.dao.PersonDao;
import by.leonovich.hibernatecrm.mappings.singletable.Employee;
import by.leonovich.hibernatecrm.mappings.singletable.Person;
import by.leonovich.hibernatecrm.mappings.singletable.Student;
import by.leonovich.hibernatecrm.tools.MagicList;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static by.leonovich.hibernatecrm.TestConstants.MAIN_LIMIT;

/**
 * Created : 07/12/2020 20:41
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public class CommonPersonDaoTest {
    protected static final Logger LOG = LoggerFactory.getLogger(CommonPersonDaoTest.class);
    protected static final Dao<Person> dao = new PersonDao();
    protected static final MagicList<Person> allPersons = new MagicList<>();
    protected static final MagicList<Employee> employees = new MagicList<>();
    protected static final MagicList<Student> students = new MagicList<>();

    @BeforeAll
    static void beforeAll() {
        /* hint to do not invoke this method more than once */
        if (CollectionUtils.isNotEmpty(allPersons)) {
            return;
        }
        employees.addAll(Stream.generate(Employee::init).limit(MAIN_LIMIT).collect(Collectors.toList()));
        students.addAll(Stream.generate(Student::init).limit(MAIN_LIMIT).collect(Collectors.toList()));

        allPersons.addAll(employees);
        allPersons.addAll(students);
        Collections.shuffle(allPersons);
        allPersons.forEach(CommonPersonDaoTest::persist);
    }

    @SneakyThrows
    protected static void persist(Person person) {
        dao.saveOrUpdate(person);
        LOG.info("{}", person);
    }
}
