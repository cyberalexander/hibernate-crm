package by.leonovich.hibernatecrm.dao.document;

import by.leonovich.hibernatecrm.common.collection.MagicList;
import by.leonovich.hibernatecrm.dao.BaseDaoTest;
import by.leonovich.hibernatecrm.dao.Dao;
import by.leonovich.hibernatecrm.dao.DrivingLicenseDao;
import by.leonovich.hibernatecrm.mappings.joinedtable.DrivingLicense;

/**
 * Created : 10/12/2020 10:12
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
class DrivingLicenseDaoTest extends CommonDocumentDaoTest implements BaseDaoTest<DrivingLicense> {
    private static final Dao<DrivingLicense> drivingLicenseDao = new DrivingLicenseDao();

    @Override
    public Dao<DrivingLicense> dao() {
        return drivingLicenseDao;
    }

    @Override
    public MagicList<DrivingLicense> entities() {
        return drivingLicenses;
    }
}

