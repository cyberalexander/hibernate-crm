package by.leonovich.hibernatecrm.dao.vehicle;

import by.leonovich.hibernatecrm.common.collection.MagicList;
import by.leonovich.hibernatecrm.dao.Dao;
import by.leonovich.hibernatecrm.dao.VehicleDao;
import by.leonovich.hibernatecrm.hibernate.HibernateUtil;
import by.leonovich.hibernatecrm.mappings.tableperclass.ElectricCar;
import by.leonovich.hibernatecrm.mappings.tableperclass.MotorCycle;
import by.leonovich.hibernatecrm.mappings.tableperclass.Vehicle;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static by.leonovich.hibernatecrm.TestConstants.MAIN_LIMIT;

/**
 * Created : 13/12/2020 15:18
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public class CommonVehicleDaoTest {
    private static final MagicList<Vehicle> allVehicles = new MagicList<>();
    protected static final MagicList<ElectricCar> electricCars = new MagicList<>();
    protected static final MagicList<MotorCycle> motorCycles = new MagicList<>();

    @BeforeAll
    @SneakyThrows
    static void beforeAll() {
        /* hint to do not invoke this method more than once */
        if (CollectionUtils.isNotEmpty(allVehicles)) {
            return;
        }
        electricCars.addAll(Stream.generate(ElectricCar::init).limit(MAIN_LIMIT).collect(Collectors.toList()));
        motorCycles.addAll(Stream.generate(MotorCycle::init).limit(MAIN_LIMIT).collect(Collectors.toList()));

        allVehicles.addAll(electricCars);
        allVehicles.addAll(motorCycles);
        Collections.shuffle(allVehicles);

        Dao<Vehicle> dao = new VehicleDao();
        for (Vehicle v : allVehicles) {
            dao.save(v);
        }
        HibernateUtil.getInstance().closeSession();

    }

    @AfterEach
    void tearDown() {
        //Approach: Session opened in DAO method; session closed here after each @test method execution
        HibernateUtil.getInstance().closeSession();
    }

    @Test
    void testDataReady() {
        MatcherAssert.assertThat(
            "Test data is not ready!",
            CollectionUtils.isEmpty(allVehicles),
            Matchers.is(Boolean.FALSE)
        );
    }
}
