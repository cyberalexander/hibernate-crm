package by.leonovich.hibernatecrm.dao.vehicle;

import by.leonovich.hibernatecrm.dao.Dao;
import by.leonovich.hibernatecrm.dao.VehicleDao;
import by.leonovich.hibernatecrm.mappings.tableperclass.ElectricCar;
import by.leonovich.hibernatecrm.mappings.tableperclass.MotorCycle;
import by.leonovich.hibernatecrm.mappings.tableperclass.Vehicle;
import by.leonovich.hibernatecrm.tools.MagicList;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class CommonVehicleDaoTest {
    protected static final Logger LOG = LoggerFactory.getLogger(CommonVehicleDaoTest.class);
    protected static final Dao<Vehicle> dao = new VehicleDao();
    protected static final MagicList<Vehicle> allVehicles = new MagicList<>();
    protected static final MagicList<ElectricCar> electricCars = new MagicList<>();
    protected static final MagicList<MotorCycle> motorCycles = new MagicList<>();

    @BeforeAll
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
        allVehicles.forEach(CommonVehicleDaoTest::persistEach);
    }

    @SneakyThrows
    private static void persistEach(Vehicle vehicle) {
        dao.saveOrUpdate(vehicle);
        LOG.info("{}", vehicle);
    }
}
