package by.leonovich.hibernatecrm.dao.vehicle;

import by.leonovich.hibernatecrm.TestConstants;
import by.leonovich.hibernatecrm.mappings.tableperclass.MotorCycle;
import lombok.SneakyThrows;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Created : 26/12/2020 21:02
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
class MotorCycleDaoTest extends CommonVehicleDaoTest {
    protected static final Logger LOG = LoggerFactory.getLogger(MotorCycleDaoTest.class);

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
    void testSaveOrUpdate_Save() {
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
    void testSaveOrUpdate_Update() {
        MotorCycle motorCycle = motorCycles.randomEntity().modify();
        dao.saveOrUpdate(motorCycle);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATE, motorCycle),
            dao.get(motorCycle.getId()),
            Matchers.equalTo(motorCycle)
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
        Serializable index = motorCycles.lastElement().incrementIdAndGet();
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
