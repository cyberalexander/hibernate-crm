package by.leonovich.hibernatecrm.dao.document;

import by.leonovich.hibernatecrm.TestConstants;
import by.leonovich.hibernatecrm.dao.PassportDao;
import by.leonovich.hibernatecrm.mappings.joinedtable.Document;
import by.leonovich.hibernatecrm.mappings.joinedtable.Passport;
import lombok.SneakyThrows;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Created : 10/12/2020 20:28
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
class PassportDaoTest extends CommonDocumentDaoTest {
    protected static final Logger LOG = LoggerFactory.getLogger(PassportDaoTest.class);

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
    void testSaveOrUpdate_Save() {
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
    void testSaveOrUpdate_Update() {
        Passport passport = passports.randomEntity().modify();
        dao.saveOrUpdate(passport);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATE, passport),
            dao.get(passport.getId()),
            Matchers.equalTo(passport)
        );
    }

    @Test
    @SneakyThrows
    void testGet() {
        Passport randomPassport = passports.randomEntity();
        MatcherAssert.assertThat(
            String.format(TestConstants.M_GET, randomPassport.getClass().getSimpleName(), randomPassport.getId()),
            dao.get(randomPassport.getId()),
            Matchers.instanceOf(Passport.class)
        );
    }

    @Test
    @SneakyThrows
    void testGetWhenNotExists() {
        Serializable index = passports.lastElement().incrementIdAndGet();
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

    @Test
    @SneakyThrows
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void testLoadWhenNotExists() {
        Serializable index = passports.lastElement().incrementIdAndGet();
        Document loaded = dao.load(index);
        Assertions.assertThrows(
            ObjectNotFoundException.class,
            loaded::getDocumentNumber,
            TestConstants.M_LOAD_EXCEPTION);
    }

    /**
     * 1. Save Passport in DB
     * 2. Get Passport from DB
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
