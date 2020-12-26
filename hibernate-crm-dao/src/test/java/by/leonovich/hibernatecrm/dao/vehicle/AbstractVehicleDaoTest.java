package by.leonovich.hibernatecrm.dao.vehicle;

import by.leonovich.hibernatecrm.dao.Dao;
import by.leonovich.hibernatecrm.dao.VehicleDao;
import by.leonovich.hibernatecrm.mappings.tableperclass.ElectricCar;
import by.leonovich.hibernatecrm.mappings.tableperclass.MotorCycle;
import by.leonovich.hibernatecrm.mappings.tableperclass.Vehicle;
import by.leonovich.hibernatecrm.tools.MagicList;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created : 13/12/2020 15:18
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class AbstractVehicleDaoTest {
    protected static final Logger LOG = LoggerFactory.getLogger(AbstractVehicleDaoTest.class);
    protected static final Random r = new Random();
    protected static final Dao<Vehicle> dao = new VehicleDao();
    protected static final MagicList<Vehicle> allVehicles = new MagicList<>();
    protected static final MagicList<ElectricCar> electricCars = new MagicList<>();
    protected static final MagicList<MotorCycle> motorCycles = new MagicList<>();

    @BeforeAll
    static void beforeAll() {
        electricCars.addAll(Stream.generate(ElectricCar::init).limit(25).collect(Collectors.toList()));
        motorCycles.addAll(Stream.generate(MotorCycle::init).limit(25).collect(Collectors.toList()));

        allVehicles.addAll(electricCars);
        allVehicles.addAll(motorCycles);
        Collections.shuffle(allVehicles);
        allVehicles.forEach(AbstractVehicleDaoTest::persistEach);
    }

    @SneakyThrows
    private static void persistEach(Vehicle vehicle) {
        dao.saveOrUpdate(vehicle);
        LOG.info("{}", vehicle);
    }
}
