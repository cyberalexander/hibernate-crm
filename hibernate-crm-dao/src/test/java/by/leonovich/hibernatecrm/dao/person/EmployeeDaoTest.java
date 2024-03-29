package by.leonovich.hibernatecrm.dao.person;

import by.leonovich.hibernatecrm.TestConstants;
import by.leonovich.hibernatecrm.TestDaoConfiguration;
import by.leonovich.hibernatecrm.common.collection.MagicList;
import by.leonovich.hibernatecrm.dao.BaseDaoTest;
import by.leonovich.hibernatecrm.dao.Dao;
import by.leonovich.hibernatecrm.dao.EmployeeDao;
import by.leonovich.hibernatecrm.mappings.singletable.Employee;
import by.leonovich.hibernatecrm.mappings.singletable.Meeting;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created : 07/12/2020 20:39
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestDaoConfiguration.class})
@Transactional
@Commit
class EmployeeDaoTest implements BaseDaoTest<Employee> {
    private static final MagicList<Employee> employees = new MagicList<>();

    @Autowired
    private Dao<Employee> dao;
    @Autowired
    private Dao<Meeting> meetingDao;

    @Test
    @SneakyThrows
    void testSaveCascade() {
        Employee emp = Employee.initWithManyToMany();
        Serializable employeeId = dao().save(emp);
        log.debug("Relation saved as well? {}", Objects.nonNull(emp.getMeetings().iterator().next().getId()));
        MatcherAssert.assertThat(
            TestConstants.M_SAVE_CASCADE,
            CollectionUtils.isNotEmpty(dao().get(employeeId).getMeetings()),
            Matchers.is(Boolean.TRUE)
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_SaveCascade() {
        Employee emp = Employee.initWithManyToMany();
        Set<Meeting> newMeetings = emp.getMeetings();
        dao().saveOrUpdate(emp);
        newMeetings.forEach(
            m -> MatcherAssert.assertThat(
                String.format(TestConstants.M_SAVE_OR_UPDATE_SAVE_CASCADE, m, emp),
                m.getId(),
                Matchers.notNullValue()
            )
        );
    }


    //@Test
    @SneakyThrows
    void testSaveOrUpdate_UpdateCascade() {
        Employee emp = Employee.initWithManyToMany();
        dao().save(emp);
        dao().saveOrUpdate(emp.modifyCascade());
        Set<Meeting> modified = emp.getMeetings();
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATED_CASCADE, modified, emp),
            dao().get(emp.getId()).getMeetings(),
            Matchers.equalTo(modified)
        );
    }

    /**
     * Employee : cascade=save-update
     * Meeting : cascade=save-update
     * In case of cascade=save-update, hibernate is not managing cascade delete operation. Developer should
     * manage it manually as in this test.
     * In this case in order to successfully delete Employee, first need to 'detach' it from Meeting(s) to avoid:
     * <code>
     * Caused by: org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException: Referential integrity constraint
     * violation: "FKEASFLIDTTFV77GWUQS4E1DI9L: PUBLIC.T_EMPLOYEE_MEETING FOREIGN KEY(F_EMPLOYEE_ID) REFERENCES
     * PUBLIC.T_PERSON(F_ID) (37)"; SQL statement: delete from T_PERSON where F_ID=? [23503-200]
     * </code>
     */
    @Test
    @SneakyThrows
    void testDeleteCascade() {
        Employee emp = Employee.initWithManyToMany();
        dao().save(emp);

        emp.getMeetings().forEach(meeting -> meeting.setEmployees(null));
        dao().saveOrUpdate(emp);

        dao().delete(emp);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_DELETE_CASCADE_AND_KEEP_RELATION, emp.getMeetings(), emp),
            meetingDao.get(emp.getMeetings().iterator().next().getId()),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testGetHighestPaidEmployee() {
        Employee emp = dao().getAll(Employee.class).stream()
            .sorted(Comparator.comparing(Employee::getSalary).reversed())
            .collect(Collectors.toList()).iterator().next();
        Employee highestPaidEmployee = ((EmployeeDao) dao()).getHighestPaidEmployee();
        log.info("{} : {}", emp.getSalary(), highestPaidEmployee.getSalary());
        MatcherAssert.assertThat(
            String.format(TestConstants.M_TEST_GET_HIGHEST_PAID_EMPLOYEE, emp, highestPaidEmployee),
            highestPaidEmployee,
            Matchers.equalTo(emp)
        );
    }

    @Test
    @SneakyThrows
    void getEmployeesOrderedBySalary() {
        List<Employee> all = dao().getAll(Employee.class).stream()
            .sorted(Comparator.comparing(Employee::getSalary))
            .collect(Collectors.toList());
        Employee lowestPaidEmployee = all.iterator().next();
        Employee highestPaidEmployee = all.get(all.size() - 1);

        List<Employee> employeesOrderedBySalary = ((EmployeeDao) dao()).getEmployeesOrderedBySalary();
        Employee lowestPaidEmployeeFromDb = employeesOrderedBySalary.iterator().next();
        Employee highestPaidEmployeeFromDb = employeesOrderedBySalary.get(all.size() - 1);

        log.info("{} : {}", lowestPaidEmployee.getSalary(), lowestPaidEmployeeFromDb.getSalary());
        MatcherAssert.assertThat(
            String.format(TestConstants.M_TEST_GET_HIGHEST_PAID_EMPLOYEE, lowestPaidEmployee, lowestPaidEmployeeFromDb),
            lowestPaidEmployee,
            Matchers.equalTo(lowestPaidEmployeeFromDb)
        );
        log.info("{} : {}", highestPaidEmployee.getSalary(), highestPaidEmployeeFromDb.getSalary());
        MatcherAssert.assertThat(
            String.format(TestConstants.M_TEST_GET_HIGHEST_PAID_EMPLOYEE, highestPaidEmployee, highestPaidEmployeeFromDb),
            highestPaidEmployee,
            Matchers.equalTo(highestPaidEmployeeFromDb)
        );
    }

    @Override
    public Dao<Employee> dao() {
        return dao;
    }

    @Override
    public MagicList<Employee> entities() {
        return employees;
    }

    @Override
    public Employee generate() {
        return Employee.initWithManyToMany();
    }
}
