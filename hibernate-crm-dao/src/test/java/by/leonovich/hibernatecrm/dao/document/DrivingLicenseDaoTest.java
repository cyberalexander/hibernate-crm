package by.leonovich.hibernatecrm.dao.document;

import by.leonovich.hibernatecrm.TestDaoConfiguration;
import by.leonovich.hibernatecrm.common.collection.MagicList;
import by.leonovich.hibernatecrm.dao.BaseDaoTest;
import by.leonovich.hibernatecrm.dao.Dao;
import by.leonovich.hibernatecrm.mappings.joinedtable.DrivingLicense;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created : 10/12/2020 10:12
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestDaoConfiguration.class})
@Transactional
@Commit
class DrivingLicenseDaoTest implements BaseDaoTest<DrivingLicense> {
    private static final MagicList<DrivingLicense> drivingLicenses = new MagicList<>();

    @Autowired
    protected Dao<DrivingLicense> dao;

    @Override
    public Dao<DrivingLicense> dao() {
        return dao;
    }

    @Override
    public MagicList<DrivingLicense> entities() {
        return drivingLicenses;
    }

    @Override
    public DrivingLicense generate() {
        return DrivingLicense.init();
    }
}

