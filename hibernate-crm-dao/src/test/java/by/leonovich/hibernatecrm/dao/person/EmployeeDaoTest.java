package by.leonovich.hibernatecrm.dao.person;

import by.leonovich.hibernatecrm.TestConstants;
import by.leonovich.hibernatecrm.mappings.singletable.Employee;
import by.leonovich.hibernatecrm.mappings.singletable.Meeting;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * Created : 07/12/2020 20:39
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
class EmployeeDaoTest extends CommonPersonDaoTest {
    protected static final Logger LOG = LoggerFactory.getLogger(EmployeeDaoTest.class);

    @Test
    @SneakyThrows
    void testPersist() {
        Employee emp = Employee.init();
        dao.persist(emp);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_PERSIST, emp),
            dao.get(emp.getId()),
            Matchers.equalTo(emp)
        );
    }

    @Test
    @SneakyThrows
    void testSave() {
        MatcherAssert.assertThat(TestConstants.M_SAVE,
            dao.save(Employee.init()),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testSaveCascade_V1() {
        Employee employee = Employee.initWithManyToMany();
        Serializable employeeId = dao.save(employee);
        Set<Meeting> meetings = Optional.of(dao.get(employeeId)).map(emp -> (Employee) emp).get().getMeetings();
        MatcherAssert.assertThat(
            TestConstants.M_SAVE,
            CollectionUtils.isNotEmpty(meetings),
            Matchers.is(Boolean.TRUE)
        );
    }

    @Test
    @SneakyThrows
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void testSaveCascade_V2() {
        Employee employee = Employee.init();
        Set<Meeting> newMeetings = Stream.generate(Meeting::init).limit(3).collect(Collectors.toSet());
        employee.setMeetings(newMeetings);
        dao.save(employee);
        newMeetings.forEach(m -> {
            MatcherAssert.assertThat(
                TestConstants.M_SAVE,
                m.getId(),
                Matchers.notNullValue()
            );
        });
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_Save() {
        Employee toSave = Employee.init();
        dao.saveOrUpdate(toSave);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_SAVE, toSave),
            dao.get(toSave.getId()),
            Matchers.equalTo(toSave)
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_Update() {
        Employee toUpdate = employees.randomEntity().modify();
        dao.saveOrUpdate(toUpdate);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATE, toUpdate),
            dao.get(toUpdate.getId()),
            Matchers.equalTo(toUpdate)
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_UpdateCascade() {
        Employee toUpdate = Employee.initWithManyToMany();
        Set<Meeting> modified = toUpdate.modifyCascade().getMeetings();
        dao.saveOrUpdate(toUpdate);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATE, modified),
            ((Employee) dao.get(toUpdate.getId())).getMeetings(),
            Matchers.equalTo(modified)
        );
    }

    @Test
    @SneakyThrows
    void testGet() {
        Employee randomEmployee = employees.randomEntity();
        LOG.info("{}", randomEmployee);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_GET, randomEmployee.getClass().getSimpleName(), randomEmployee.getId()),
            dao.get(randomEmployee.getId()),
            Matchers.instanceOf(Employee.class)
        );
    }

    @Test
    @SneakyThrows
    void testLoad() {
        Serializable randomIndex = employees.randomEntity().getId();
        MatcherAssert.assertThat(
            String.format(TestConstants.M_LOAD, Employee.class.getSimpleName(), randomIndex),
            dao.load(randomIndex),
            Matchers.notNullValue()
        );
    }
}
