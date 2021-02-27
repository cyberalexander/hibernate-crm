package by.leonovich.hibernatecrm.dao.vehicle;

import by.leonovich.hibernatecrm.common.collection.MagicList;
import by.leonovich.hibernatecrm.dao.BaseDaoTest;
import by.leonovich.hibernatecrm.dao.Dao;
import by.leonovich.hibernatecrm.mappings.tableperclass.ElectricCar;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Created : 13/12/2020 15:22
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations= "classpath:DaoContext.xml")
class ElectricCarDaoTest implements BaseDaoTest<ElectricCar> {
    private static final MagicList<ElectricCar> electricCars = new MagicList<>();

    @Autowired
    private Dao<ElectricCar> dao;

    @Override
    public Dao<ElectricCar> dao() {
        return dao;
    }

    @Override
    public MagicList<ElectricCar> entities() {
        return electricCars;
    }

    @Override
    public ElectricCar generate() {
        return ElectricCar.init();
    }
}
