package by.leonovich.hibernatecrm.dao.document;

import by.leonovich.hibernatecrm.TestDaoConfiguration;
import by.leonovich.hibernatecrm.common.collection.MagicList;
import by.leonovich.hibernatecrm.dao.BaseDaoTest;
import by.leonovich.hibernatecrm.dao.Dao;
import by.leonovich.hibernatecrm.mappings.joinedtable.Passport;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created : 10/12/2020 20:28
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
class PassportDaoTest implements BaseDaoTest<Passport> {
    private final MagicList<Passport> passports = new MagicList<>();

    @Autowired
    protected Dao<Passport> dao;

    @Override
    public Dao<Passport> dao() {
        return dao;
    }

    @Override
    public MagicList<Passport> entities() {
        return passports;
    }

    @Override
    public Passport generate() {
        return Passport.init();
    }
}
