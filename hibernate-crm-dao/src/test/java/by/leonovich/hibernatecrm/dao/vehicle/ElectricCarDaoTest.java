package by.leonovich.hibernatecrm.dao.vehicle;

import by.leonovich.hibernatecrm.common.collection.MagicList;
import by.leonovich.hibernatecrm.dao.BaseDaoTest;
import by.leonovich.hibernatecrm.dao.Dao;
import by.leonovich.hibernatecrm.dao.ElectricCarDao;
import by.leonovich.hibernatecrm.mappings.tableperclass.ElectricCar;

/**
 * Created : 13/12/2020 15:22
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
class ElectricCarDaoTest extends CommonVehicleDaoTest implements BaseDaoTest<ElectricCar> {
    private static final Dao<ElectricCar> electricCarDao = new ElectricCarDao();

    @Override
    public Dao<ElectricCar> dao() {
        return electricCarDao;
    }

    @Override
    public MagicList<ElectricCar> entities() {
        return electricCars;
    }
}
