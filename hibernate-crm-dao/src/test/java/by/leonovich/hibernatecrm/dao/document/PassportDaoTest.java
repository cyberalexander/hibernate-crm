package by.leonovich.hibernatecrm.dao.document;

import by.leonovich.hibernatecrm.common.collection.MagicList;
import by.leonovich.hibernatecrm.dao.BaseDaoTest;
import by.leonovich.hibernatecrm.dao.Dao;
import by.leonovich.hibernatecrm.dao.PassportDao;
import by.leonovich.hibernatecrm.mappings.joinedtable.Passport;

/**
 * Created : 10/12/2020 20:28
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
class PassportDaoTest extends CommonDocumentDaoTest implements BaseDaoTest<Passport> {
    private static final Dao<Passport> passportDao = new PassportDao();

    @Override
    public Dao<Passport> dao() {
        return passportDao;
    }

    @Override
    public MagicList<Passport> entities() {
        return passports;
    }
}
