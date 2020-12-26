package by.leonovich.hibernatecrm.dao.document;

import by.leonovich.hibernatecrm.TestConstants;
import by.leonovich.hibernatecrm.dao.Dao;
import by.leonovich.hibernatecrm.dao.DrivingLicenseDao;
import by.leonovich.hibernatecrm.mappings.joinedtable.Document;
import by.leonovich.hibernatecrm.mappings.joinedtable.DrivingLicense;
import by.leonovich.hibernatecrm.tools.RandomString;
import lombok.SneakyThrows;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created : 10/12/2020 10:12
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
class DrivingLicenseDaoTest extends AbstractDocumentDaoTest {

    @Test
    @SneakyThrows
    void testPersist() {
        DrivingLicense d = DrivingLicense.init();
        dao.persist(d);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_PERSIST, d),
            dao.get(d.getId()),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testSave() {
        MatcherAssert.assertThat(
            TestConstants.M_SAVE,
            dao.save(DrivingLicense.init()),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdateSave() {
        DrivingLicense toSave = DrivingLicense.init();
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
        DrivingLicense toUpdate = drivingLicenses.randomEntity();
        toUpdate.setDocumentNumber("UPDATE_" + RandomString.DOCUMENT_NUMBER.get() + "_" + toUpdate.getId());
        toUpdate.setInternational(new Random().nextBoolean());
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
        DrivingLicense randomLicense = drivingLicenses.randomEntity();
        MatcherAssert.assertThat(
            String.format(TestConstants.M_GET, randomLicense.getClass().getSimpleName(), randomLicense.getId()),
            dao.get(randomLicense.getId()),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testLoad() {
        Serializable randomIndex = drivingLicenses.randomEntity().getId();
        MatcherAssert.assertThat(
            String.format(TestConstants.M_LOAD, DrivingLicense.class.getSimpleName(), randomIndex),
            dao.load(randomIndex),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testDelete() {
        DrivingLicense toDelete = DrivingLicense.init();

        dao.saveOrUpdate(toDelete); /* 1. Save object in DB */
        Assertions.assertNotNull(toDelete.getId(), TestConstants.M_SAVE);
        dao.delete(toDelete); /* 2. Delete object from DB */

        MatcherAssert.assertThat(String.format(TestConstants.M_DELETE, toDelete),
            dao.get(toDelete.getId()), /* 3. Try to get deleted object from DB */
            Matchers.nullValue()
        );
    }

    @Test
    @SneakyThrows
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void testGetAll() {
        Dao<DrivingLicense> drivingLicenseDao = new DrivingLicenseDao();
        List<Serializable> queried = drivingLicenseDao.getAll(DrivingLicense.class).stream()
            .map(DrivingLicense::getId)
            .collect(Collectors.toList());
        drivingLicenses.stream().map(DrivingLicense::getId).forEach(p ->
            MatcherAssert.assertThat(
                String.format(TestConstants.M_GET_ALL, DrivingLicense.class.getSimpleName(), p),
                queried.contains(p),
                Matchers.is(Boolean.TRUE)
            ));
    }

    @Test
    @SneakyThrows
    void testGetIds() {
        Dao<DrivingLicense> drivingLicenseDao = new DrivingLicenseDao();
        List<Serializable> passportIdsOnly = drivingLicenseDao.getIds();
        Optional<Document> notDrivingLicense = passportIdsOnly.stream()
            .map(this::daoGet)
            .filter(dLicense -> !(dLicense instanceof DrivingLicense))
            .findFirst();
        MatcherAssert.assertThat(
            String.format(TestConstants.M_GET_PARTICULAR_TYPE_IDS, notDrivingLicense, DrivingLicense.class.getSimpleName()),
            notDrivingLicense.isEmpty(),
            Matchers.is(Boolean.TRUE)
        );
    }
}

