package by.leonovich.hibernatecrm.dao.university;

import by.leonovich.hibernatecrm.TestConstants;
import by.leonovich.hibernatecrm.mappings.singletable.Student;
import by.leonovich.hibernatecrm.mappings.singletable.University;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.persistence.PersistenceException;
import java.io.Serializable;
import java.util.Set;

/**
 * Created : 13/12/2020 20:24
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
class UniversityDaoTest extends CommonUniversityDaoTest {

    @Test
    @SneakyThrows
    void testPersist() {
        University university = University.init();
        universityDao.persist(university);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_PERSIST, university),
            universityDao.get(university.getId()),
            Matchers.equalTo(university)
        );
    }

    @Test
    @SneakyThrows
    void testSave() {
        University university = University.init();
        MatcherAssert.assertThat(
            TestConstants.M_SAVE,
            universityDao.save(university),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_Save() {
        University toSave = University.init();
        universityDao.saveOrUpdate(toSave);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_SAVE, toSave),
            universityDao.get(toSave.getId()),
            Matchers.equalTo(toSave)
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_SaveCascade() {
        University u = University.initWithOneToMany();
        universityDao.saveOrUpdate(u);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_SAVE_CASCADE, u.getStudents()),
            daoGet(u.getId()).getStudents().size(),
            Matchers.is(3)
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_Update() {
        University toUpdate = universities.randomEntity();
        universityDao.saveOrUpdate(toUpdate.modify());
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATE, toUpdate),
            universityDao.get(toUpdate.getId()),
            Matchers.equalTo(toUpdate)
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_UpdateCascade() {
        University toUpdate = University.initWithOneToMany();
        universityDao.saveOrUpdate(toUpdate);
        Set<Student> updated = toUpdate.modifyCascade().getStudents();
        universityDao.saveOrUpdate(toUpdate);
        Set<Student> queried = universityDao.get(toUpdate.getId()).getStudents();
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATE, updated),
            queried,
            Matchers.equalTo(updated)
        );
    }

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void testSaveOrUpdate_UpdateUniversityToNull() {
        universities.stream()
            .filter(u -> CollectionUtils.isNotEmpty(u.getStudents()))
            .findFirst()
            .ifPresentOrElse(uWithStudents -> {
                    uWithStudents.getStudents().forEach(s -> {
                        s.setUniversity(null);
                        studentDaoSaveOrUpdate(s);
                    });
                    Set<Student> queried = daoGet(uWithStudents.getId()).getStudents();
                    MatcherAssert.assertThat(
                        String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATE, queried),
                        CollectionUtils.isEmpty(queried),
                        Matchers.is(Boolean.TRUE)
                    );
                }, () -> LOG.warn("Test skipped cause University containing students not found in test data!")
            );
    }

    @Test
    @SneakyThrows
    void testGet() {
        University randomUniversity = universities.randomEntity();
        LOG.info("{}", randomUniversity);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_GET, randomUniversity.getClass().getSimpleName(), randomUniversity.getId()),
            universityDao.get(randomUniversity.getId()),
            Matchers.instanceOf(University.class)
        );
    }

    @Test
    @SneakyThrows
    void testGetWhenNotExists() {
        Serializable index = universities.lastElement().getId() + 300L;
        MatcherAssert.assertThat(
            String.format(TestConstants.M_GET_NOT_EXISTS, index),
            universityDao.get(index),
            Matchers.nullValue()
        );
    }

    @Test
    @SneakyThrows
    void testLoad() {
        Serializable randomIndex = universities.randomEntity().getId();
        MatcherAssert.assertThat(
            String.format(TestConstants.M_LOAD, University.class.getSimpleName(), randomIndex),
            universityDao.load(randomIndex),
            Matchers.notNullValue()
        );
    }

    /**
     * Important to delete reference to University to avoid 'constraint violation' exceptions at the time
     * of delete University. I should manage deletion on my own as I set cascade="save-update", so hibernate will
     * not delete students at the time of University deletion, but will throw exception, if any student will
     * have reference to university to be deleted
     */
    @Test
    @SneakyThrows
    void testDelete() {
        University toDelete = University.initWithOneToMany();
        universityDao.saveOrUpdate(toDelete);
        Assertions.assertNotNull(toDelete.getId(), TestConstants.M_SAVE);

        for (Student st : toDelete.getStudents()) {
            st.setUniversity(null);
            dao.saveOrUpdate(st);
        }
        universityDao.delete(toDelete);

        MatcherAssert.assertThat(String.format(TestConstants.M_DELETE, toDelete),
            universityDao.get(toDelete.getId()),
            Matchers.nullValue()
        );
    }

    /**
     * In case if system trying to delete University, and at the same time Student(s) has reference to it, then
     * Exception as below will be thrown
     * <code>
     *     Caused by: org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException: Referential integrity constraint
     *     violation: "FKIAQXXAFM5M20FPXP7HAWBDRYI: PUBLIC.T_PERSON FOREIGN KEY(F_UNIVERSITY_ID) REFERENCES PUBLIC.T_UNIVERSITY(F_ID) (9)";
     *     SQL statement: delete from T_UNIVERSITY where F_ID=? [23503-200]
     * </code>
     */
    @Test
    @SneakyThrows
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void testDelete_ConstraintExists() {
        University toDelete = University.initWithOneToMany();
        universityDao.saveOrUpdate(toDelete);
        Assertions.assertNotNull(toDelete.getId(), TestConstants.M_SAVE);
        Assertions.assertThrows(
            PersistenceException.class,
            () -> universityDao.delete(toDelete),
            String.format(TestConstants.M_EXCEPTION_EXPECTED, toDelete.getStudents()));
    }

    @SneakyThrows
    private void studentDaoSaveOrUpdate(Student s) {
        dao.saveOrUpdate(s);
    }

    @SneakyThrows
    private University daoGet(Serializable id) {
        return universityDao.get(id);
    }
}
