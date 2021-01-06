package by.leonovich.hibernatecrm.dao.person;

import by.leonovich.hibernatecrm.common.collection.MagicList;
import by.leonovich.hibernatecrm.dao.Dao;
import by.leonovich.hibernatecrm.dao.PersonDao;
import by.leonovich.hibernatecrm.hibernate.HibernateUtil;
import by.leonovich.hibernatecrm.mappings.singletable.Employee;
import by.leonovich.hibernatecrm.mappings.singletable.Person;
import by.leonovich.hibernatecrm.mappings.singletable.Student;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
    protected static final Dao<Person> personDao = new PersonDao();
    protected static final MagicList<Person> allPersons = new MagicList<>();
    protected static final MagicList<Employee> employees = new MagicList<>();
    protected static final MagicList<Student> students = new MagicList<>();

    @BeforeAll
    @SneakyThrows
    static void beforeAll() {
        /* hint to do not invoke this method more than once for every children class */
        if (CollectionUtils.isNotEmpty(allPersons)) {
            return;
        }
        employees.addAll(Stream.generate(Employee::initWithManyToMany).limit(MAIN_LIMIT).collect(Collectors.toList()));
        students.addAll(Stream.generate(Student::initWithManyToOne).limit(MAIN_LIMIT).collect(Collectors.toList()));

        allPersons.addAll(employees);
        allPersons.addAll(students);
        Collections.shuffle(allPersons);

        for (Person p : allPersons) {
            personDao.save(p);
        }
        HibernateUtil.getInstance().closeSession();
    }

    @AfterEach
    void tearDown() {
        HibernateUtil.getInstance().closeSession();
    }

    @Test
    void testDataReady() {
        MatcherAssert.assertThat(
            "Test data is not ready!",
            CollectionUtils.isEmpty(allPersons),
            Matchers.is(Boolean.FALSE)
        );
    }
}
