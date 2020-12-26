package by.leonovich.hibernatecrm.dao.person;

import by.leonovich.hibernatecrm.TestConstants;
import by.leonovich.hibernatecrm.mappings.singletable.Student;
import by.leonovich.hibernatecrm.mappings.singletable.University;
import by.leonovich.hibernatecrm.tools.RandomString;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.stream.Collectors;

/**
 * Created : 13/12/2020 20:24
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
class UniversityDaoTest extends AbstractPersonDaoTest {

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
    void testSaveOrUpdateSave() {
        University toSave = University.init();
        universityDao.saveOrUpdate(toSave);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_SAVE, toSave),
            universityDao.get(toSave.getId()),
            Matchers.equalTo(toSave)
        );
    }

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void testSaveOrUpdateUpdateV1() {
        universities.stream()
            .filter(university -> CollectionUtils.isNotEmpty(university.getStudents()))
            .findFirst()
            .ifPresentOrElse(uWithStudents -> {
                    uWithStudents.getStudents().forEach(s -> {
                        s.setUniversity(null);
                        studentDaoSaveOrUpdate(s);
                    });
                    MatcherAssert.assertThat(
                        String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATE, uWithStudents.getStudents().size()),
                        universityDaoGet(uWithStudents.getId()).getStudents().size(),
                        Matchers.is(0)
                    );
                }, () -> LOG.warn("University#testSaveOrUpdateUpdate skipped cause University with students not found.")
            );
    }

    @Test
    @SneakyThrows
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void testSaveOrUpdateUpdateV2() {
        University u = University.init();
        universityDao.saveOrUpdate(u);
        for (Student s : AbstractPersonDaoTest.students.stream().limit(5).collect(Collectors.toList())) {
            s.setUniversity(u);
            u.getStudents().add(s);
            dao.saveOrUpdate(s);
        }
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATE, u.getStudents().size()),
            universityDaoGet(u.getId()).getStudents().size(),
            Matchers.is(5)
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdateUpdateName() {
        University toUpdate = universities.randomEntity();
        toUpdate.setUniversityName("UPDATE_" + RandomString.DEFAULT.get() + "_" + toUpdate.getId());
        universityDao.saveOrUpdate(toUpdate);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATE, toUpdate),
            universityDao.get(toUpdate.getId()),
            Matchers.equalTo(toUpdate)
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

    @SneakyThrows
    private void studentDaoSaveOrUpdate(Student s) {
        dao.saveOrUpdate(s);
    }

    @SneakyThrows
    private University universityDaoGet(Serializable id) {
        return universityDao.get(id);
    }
}
