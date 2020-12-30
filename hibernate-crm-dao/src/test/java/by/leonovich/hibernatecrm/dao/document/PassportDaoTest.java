package by.leonovich.hibernatecrm.dao.document;

import by.leonovich.hibernatecrm.TestConstants;
import by.leonovich.hibernatecrm.dao.PassportDao;
import by.leonovich.hibernatecrm.mappings.joinedtable.Document;
import by.leonovich.hibernatecrm.mappings.joinedtable.Passport;
import by.leonovich.hibernatecrm.tools.RandomString;
import lombok.SneakyThrows;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static by.leonovich.hibernatecrm.TestConstants.UPDATE_PREFIX;
/**
 * Created : 10/12/2020 20:28
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
class PassportDaoTest extends CommonDocumentDaoTest {

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
        Passport toUpdate = passports.randomEntity();
        toUpdate.setPassportNumber(UPDATE_PREFIX + RandomString.PASSPORT_NUMBER.get() + "_" + toUpdate.getId());
        dao.saveOrUpdate(toUpdate);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATE, toUpdate),
            dao.get(toUpdate.getId()),
            Matchers.equalTo(toUpdate)
        );
    }

    @Test
    @SneakyThrows
    void testGet() {
        Passport randomPassport = passports.randomEntity();
        LOG.info("{}", randomPassport);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_GET, randomPassport.getClass().getSimpleName(), randomPassport.getId()),
            dao.get(randomPassport.getId()),
            Matchers.instanceOf(Passport.class)
        );
    }

    @Test
    @SneakyThrows
    void testGetWhenNotExists() {
        Serializable index = passports.lastElement().getId() + 300L;
        MatcherAssert.assertThat(
            String.format(TestConstants.M_GET_NOT_EXISTS, index),
            dao.get(index),
            Matchers.nullValue()
        );
    }

    @Test
    @SneakyThrows
    void testLoad() {
        Serializable randomIndex = passports.randomEntity().getId();
        MatcherAssert.assertThat(
            String.format(TestConstants.M_LOAD, Passport.class.getSimpleName(), randomIndex),
            dao.load(randomIndex),
            Matchers.notNullValue()
        );
    }

    /**
     * Method {@link Session#load} is different from {@link Session#get}
     * In case of "get", when object not exists in database, hibernate returns null. When in case of "load"
     * hibernate will not query object immediately but will just return proxy. And when we try to access any property
     * of that proxy, hibernate will immediately execute select to database and throw {@link ObjectNotFoundException}
     * if object won't be found there.
     */
    @Test
    @SneakyThrows
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void testLoadWhenNotExists() {
        Serializable index = passports.lastElement().getId() + 300L;
        Document loaded = dao.load(index);
        Assertions.assertThrows(
            ObjectNotFoundException.class,
            loaded::getDocumentNumber,
            TestConstants.M_LOAD_EXCEPTION);
    }

    /**
     * 1. Save Passport in DB
     * 2. Passport Meeting from DB
     * 3. Assert that Passport does not exists in DB any more
     */
    @Test
    @SneakyThrows
    void testDelete() {
        Passport toDelete = Passport.init();

        dao.saveOrUpdate(toDelete); //[1]
        Assertions.assertNotNull(toDelete.getId(), TestConstants.M_SAVE);
        dao.delete(toDelete); //[2]

        MatcherAssert.assertThat(String.format(TestConstants.M_DELETE, toDelete),
            dao.get(toDelete.getId()), //[3]
            Matchers.nullValue()
        );
    }

    @Test
    @SneakyThrows
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void testGetAll() {
        List<Serializable> queried = new PassportDao().getAll(Passport.class).stream()
            .map(Passport::getId)
            .collect(Collectors.toList());
        passports.stream().map(Passport::getId).forEach(p ->
            MatcherAssert.assertThat(
                String.format(TestConstants.M_GET_ALL, Passport.class.getSimpleName(), p),
                queried.contains(p),
                Matchers.is(Boolean.TRUE)
            ));
    }

    @Test
    @SneakyThrows
    void testGetIds() {
        Optional<Document> notPassport = new PassportDao().getIds().stream()
            .map(this::daoGet)
            .filter(passport -> !(passport instanceof Passport))
            .findFirst();
        MatcherAssert.assertThat(
            String.format(TestConstants.M_GET_PARTICULAR_TYPE_IDS, notPassport, Passport.class.getSimpleName()),
            notPassport.isEmpty(),
            Matchers.is(Boolean.TRUE)
        );
    }
}
