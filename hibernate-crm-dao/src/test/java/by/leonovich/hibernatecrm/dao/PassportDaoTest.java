package by.leonovich.hibernatecrm.dao;

import by.leonovich.hibernatecrm.TestConstants;
import by.leonovich.hibernatecrm.mappings.joinedtable.Passport;
import by.leonovich.hibernatecrm.tools.RandomString;
import lombok.SneakyThrows;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Created : 10/12/2020 20:28
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
class PassportDaoTest extends AbstractDocumentDaoTest {

    @Test
    @SneakyThrows
    void testPersist() {
        Passport passport = Passport.init();
        dao.persist(passport);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_PERSIST, passport),
            dao.get(passport.getId()),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testSave() {
        MatcherAssert.assertThat(
            TestConstants.M_SAVE,
            dao.save(Passport.init()),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdateSave() {
        Passport toSave = Passport.init();
        dao.saveOrUpdate(toSave);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_SAVE, toSave),
            dao.get(toSave.getId()),
            Matchers.equalTo(toSave)
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdateUpdate() {
        Passport toUpdate = randomPassport();
        toUpdate.setPassportNumber("UPDATE_" + RandomString.PASSPORT_NUMBER.get() + "_" + toUpdate.getId());
        dao.saveOrUpdate(toUpdate);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATE, toUpdate),
            dao.get(toUpdate.getId()),
            Matchers.equalTo(toUpdate)
        );
    }

    @Test
    @SneakyThrows
    void testGetPassport() {
        Passport randomPassport = randomPassport();
        LOG.info("{}", randomPassport);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_GET, randomPassport.getClass().getSimpleName(), randomPassport.getId()),
            dao.get(randomPassport.getId()),
            Matchers.instanceOf(Passport.class)
        );
    }

    public Passport randomPassport() {
        return (Passport) allDocuments.stream().filter(p -> p instanceof Passport).findFirst().get();
    }
}
