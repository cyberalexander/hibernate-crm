package by.leonovich.hibernatecrm.dao.vehicle;

import by.leonovich.hibernatecrm.TestConstants;
import by.leonovich.hibernatecrm.mappings.tableperclass.ElectricCar;
import lombok.SneakyThrows;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Created : 13/12/2020 15:22
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
class ElectricCarDaoTest extends CommonVehicleDaoTest {
    protected static final Logger LOG = LoggerFactory.getLogger(ElectricCarDaoTest.class);

    @Test
    @SneakyThrows
    void testPersist() {
        ElectricCar car = ElectricCar.init();
        dao.persist(car);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_PERSIST, car),
            dao.get(car.getId()),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testSave() {
        MatcherAssert.assertThat(
            TestConstants.M_SAVE,
            dao.save(ElectricCar.init()),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_Save() {
        ElectricCar toSave = ElectricCar.init();
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
        ElectricCar car = electricCars.randomEntity().modify();
        dao.saveOrUpdate(car);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATE, car),
            dao.get(car.getId()),
            Matchers.equalTo(car)
        );
    }

    @Test
    @SneakyThrows
    void testGet() {
        Serializable randomId = electricCars.randomEntity().getId();
        MatcherAssert.assertThat(
            String.format(TestConstants.M_GET, ElectricCar.class.getSimpleName(), randomId),
            dao.get(randomId),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testGetWhenNotExists() {
        Serializable index = electricCars.lastElement().incrementIdAndGet();
        MatcherAssert.assertThat(
            String.format(TestConstants.M_GET_NOT_EXISTS, index),
            dao.get(index),
            Matchers.nullValue()
        );
    }

    @Test
    @SneakyThrows
    void testLoad() {
        Serializable randomId = electricCars.randomEntity().getId();
        MatcherAssert.assertThat(
            String.format(TestConstants.M_LOAD, ElectricCar.class.getSimpleName(), randomId),
            dao.load(randomId),
            Matchers.notNullValue()
        );
    }
}
