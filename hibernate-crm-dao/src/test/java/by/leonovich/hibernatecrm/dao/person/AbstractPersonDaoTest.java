package by.leonovich.hibernatecrm.dao.person;

import by.leonovich.hibernatecrm.dao.Dao;
import by.leonovich.hibernatecrm.dao.PersonDao;
import by.leonovich.hibernatecrm.dao.UniversityDao;
import by.leonovich.hibernatecrm.mappings.singletable.Employee;
import by.leonovich.hibernatecrm.mappings.singletable.Person;
import by.leonovich.hibernatecrm.mappings.singletable.Student;
import by.leonovich.hibernatecrm.mappings.singletable.University;
import by.leonovich.hibernatecrm.tools.MagicList;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
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
    protected static final Dao<Person> dao = new PersonDao();
    protected static final MagicList<Person> allPersons = new MagicList<>();
    protected static final MagicList<Employee> employees = new MagicList<>();
    protected static final MagicList<Student> students = new MagicList<>();

    protected static MagicList<University> universities = new MagicList<>();
    protected static final Dao<University> universityDao = new UniversityDao();

    @BeforeAll
    static void beforeAll() {
        universities.addAll(Stream.generate(University::init).limit(5)
            .map(AbstractPersonDaoTest::persistUniversity)
            .collect(Collectors.toList())
        );

        employees.addAll(Stream.generate(Employee::init).limit(10).collect(Collectors.toList()));
        students.addAll(Stream.generate(Student::init)
            .peek(student -> {
                University u = universities.randomEntity();
                student.setUniversity(u);
                u.getStudents().add(student);
            })
            .limit(10)
            .collect(Collectors.toList())
        );

        allPersons.addAll(employees);
        allPersons.addAll(students);
        Collections.shuffle(allPersons);
        allPersons.forEach(AbstractPersonDaoTest::persistEach);
    }

    @SneakyThrows
    private static Person persistEach(Person person) {
        dao.saveOrUpdate(person);
        LOG.info("{}", person);
        return person;
    }

    @SneakyThrows
    private static University persistUniversity(University university) {
        universityDao.saveOrUpdate(university);
        LOG.info("{}", university);
        return university;
    }
}
