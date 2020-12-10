package by.leonovich.hibernatecrm.dao;

import by.leonovich.hibernatecrm.TestConstants;
import by.leonovich.hibernatecrm.mappings.singletable.Employee;
import by.leonovich.hibernatecrm.tools.RandomString;
import lombok.SneakyThrows;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Created : 07/12/2020 20:39
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
class EmployeeDaoTest extends AbstractPersonDaoTest {

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
    void testSaveOrUpdateSave() {
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
    void testSaveOrUpdateUpdate() {
        Employee toUpdate = randomEmployee();
        toUpdate.setCompany("UPDATE_" + RandomString.COMPANY.get() + "_" + toUpdate.getId());
        dao.saveOrUpdate(toUpdate);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATE, toUpdate),
            dao.get(toUpdate.getId()),
            Matchers.equalTo(toUpdate)
        );
    }

    @Test
    @SneakyThrows
    void testGetEmployee() {
        Employee randomEmployee = randomEmployee();
        LOG.info("{}", randomEmployee);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_GET, randomEmployee.getClass().getSimpleName(), randomEmployee.getId()),
            dao.get(randomEmployee.getId()),
            Matchers.instanceOf(Employee.class)
        );
    }

    public Employee randomEmployee() {
        return (Employee) allPersons.stream().filter(p -> p instanceof Employee).findFirst().get();
    }
}
