package by.leonovich.hibernatecrm.dao.vehicle;

import by.leonovich.hibernatecrm.TestConstants;
import by.leonovich.hibernatecrm.mappings.tableperclass.MotorCycle;
import by.leonovich.hibernatecrm.tools.RandomNumber;
import by.leonovich.hibernatecrm.tools.RandomString;
import lombok.SneakyThrows;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static by.leonovich.hibernatecrm.TestConstants.UPDATE_PREFIX;

/**
 * Created : 26/12/2020 21:02
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
class MotorCycleDaoTest extends CommonVehicleDaoTest {

    @Test
    @SneakyThrows
    void testPersist() {
        MotorCycle motorCycle = MotorCycle.init();
        dao.persist(motorCycle);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_PERSIST, motorCycle),
            dao.get(motorCycle.getId()),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testSave() {
        MatcherAssert.assertThat(
            TestConstants.M_SAVE,
            dao.save(MotorCycle.init()),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdateSave() {
        MotorCycle motorCycle = MotorCycle.init();
        dao.saveOrUpdate(motorCycle);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_SAVE, motorCycle),
            dao.get(motorCycle.getId()),
            Matchers.equalTo(motorCycle)
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdateUpdate() {
        MotorCycle toUpdate = motorCycles.randomEntity();
        toUpdate.setManufacturer(UPDATE_PREFIX + RandomString.MANUFACTURER.get() + "_" + toUpdate.getId());
        toUpdate.setTankCapacity(RandomNumber.ENGINE_VOLUME.get());
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
        Serializable randomId = motorCycles.randomEntity().getId();
        MatcherAssert.assertThat(
            String.format(TestConstants.M_GET, MotorCycle.class.getSimpleName(), randomId),
            dao.get(randomId),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testGetWhenNotExists() {
        Serializable index = motorCycles.lastElement().getId() + 300L;
        MatcherAssert.assertThat(
            String.format(TestConstants.M_GET_NOT_EXISTS, index),
            dao.get(index),
            Matchers.nullValue()
        );
    }

    @Test
    @SneakyThrows
    void testLoad() {
        Serializable randomId = motorCycles.randomEntity().getId();
        MatcherAssert.assertThat(
            String.format(TestConstants.M_LOAD, MotorCycle.class.getSimpleName(), randomId),
            dao.load(randomId),
            Matchers.notNullValue()
        );
    }
}
