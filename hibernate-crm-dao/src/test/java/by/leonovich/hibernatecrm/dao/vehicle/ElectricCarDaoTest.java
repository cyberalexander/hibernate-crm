package by.leonovich.hibernatecrm.dao.vehicle;

import by.leonovich.hibernatecrm.TestConstants;
import by.leonovich.hibernatecrm.mappings.tableperclass.ElectricCar;
import by.leonovich.hibernatecrm.mappings.tableperclass.Vehicle;
import by.leonovich.hibernatecrm.tools.RandomNumber;
import by.leonovich.hibernatecrm.tools.RandomString;
import lombok.SneakyThrows;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

/**
 * Created : 13/12/2020 15:22
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
class ElectricCarDaoTest extends AbstractVehicleDaoTest {

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
    void testSaveOrUpdateSave() {
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
    void testSaveOrUpdateUpdate() {
        ElectricCar toUpdate = electricCars.randomEntity();
        toUpdate.setManufacturer("UPDATE_" + RandomString.MANUFACTURER.get() + "_" + toUpdate.getId());
        toUpdate.setBatteryCapacity(RandomNumber.BATTERY_CAPACITY.get());
        dao.saveOrUpdate(toUpdate);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATE, toUpdate),
            dao.get(toUpdate.getId()),
            Matchers.equalTo(toUpdate)
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
    void testGetWhenExists() {
        Serializable index = electricCars.lastElement().getId() + 300L;
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
            String.format(TestConstants.M_LOAD, Vehicle.class.getSimpleName(), randomId),
            dao.load(randomId),
            Matchers.notNullValue()
        );
    }
}
