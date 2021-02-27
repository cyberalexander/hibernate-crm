package by.leonovich.hibernatecrm.dao.vehicle;

import by.leonovich.hibernatecrm.common.collection.MagicList;
import by.leonovich.hibernatecrm.dao.BaseDaoTest;
import by.leonovich.hibernatecrm.dao.Dao;
import by.leonovich.hibernatecrm.mappings.tableperclass.MotorCycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Created : 26/12/2020 21:02
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations= "classpath:DaoContext.xml")
class MotorCycleDaoTest implements BaseDaoTest<MotorCycle> {
    private static final MagicList<MotorCycle> motorcycles = new MagicList<>();

    @Autowired
    private Dao<MotorCycle> dao;

    @Override
    public Dao<MotorCycle> dao() {
        return dao;
    }

    @Override
    public MagicList<MotorCycle> entities() {
        return motorcycles;
    }

    @Override
    public MotorCycle generate() {
        return MotorCycle.init();
    }
}
