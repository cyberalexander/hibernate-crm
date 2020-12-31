package by.leonovich.hibernatecrm.dao.document;

import by.leonovich.hibernatecrm.TestConstants;
import by.leonovich.hibernatecrm.dao.DrivingLicenseDao;
import by.leonovich.hibernatecrm.mappings.joinedtable.Document;
import by.leonovich.hibernatecrm.mappings.joinedtable.DrivingLicense;
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
 * Created : 10/12/2020 10:12
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
class DrivingLicenseDaoTest extends CommonDocumentDaoTest {
    protected static final Logger LOG = LoggerFactory.getLogger(DrivingLicenseDaoTest.class);

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
    void testSaveOrUpdate_Save() {
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
    void testSaveOrUpdate_Update() {
        DrivingLicense drivingLicense = drivingLicenses.randomEntity().modify();
        dao.saveOrUpdate(drivingLicense);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATE, drivingLicense),
            dao.get(drivingLicense.getId()),
            Matchers.equalTo(drivingLicense)
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
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void testLoadWhenNotExists() {
        Serializable index = drivingLicenses.lastElement().incrementIdAndGet();
        Document loaded = dao.load(index);
        Assertions.assertThrows(
            ObjectNotFoundException.class,
            loaded::getDocumentNumber,
            TestConstants.M_LOAD_EXCEPTION);
    }

    /**
     * 1. Save DrivingLicense in DB
     * 2. Get DrivingLicense from DB
     * 3. Assert that DrivingLicense does not exists in DB any more
     */
    @Test
    @SneakyThrows
    void testDelete() {
        DrivingLicense toDelete = DrivingLicense.init();

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
        List<Serializable> queried = new DrivingLicenseDao().getAll(DrivingLicense.class).stream()
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
        Optional<Document> notDrivingLicense = new DrivingLicenseDao().getIds().stream()
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

