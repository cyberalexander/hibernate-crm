package by.leonovich.hibernatecrm.dao.vehicle;

import by.leonovich.hibernatecrm.common.collection.MagicList;
import by.leonovich.hibernatecrm.dao.BaseDaoTest;
import by.leonovich.hibernatecrm.dao.Dao;
import by.leonovich.hibernatecrm.dao.MotorCycleDao;
import by.leonovich.hibernatecrm.mappings.tableperclass.MotorCycle;

/**
 * Created : 26/12/2020 21:02
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
class MotorCycleDaoTest extends CommonVehicleDaoTest implements BaseDaoTest<MotorCycle> {
    private static final Dao<MotorCycle> motorCycleDao = new MotorCycleDao();

    @Override
    public Dao<MotorCycle> dao() {
        return motorCycleDao;
    }

    @Override
    public MagicList<MotorCycle> entities() {
        return motorCycles;
    }
}
