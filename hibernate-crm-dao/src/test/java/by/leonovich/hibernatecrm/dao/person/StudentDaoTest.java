package by.leonovich.hibernatecrm.dao.person;

import by.leonovich.hibernatecrm.TestConstants;
import by.leonovich.hibernatecrm.TestDaoConfiguration;
import by.leonovich.hibernatecrm.common.collection.MagicList;
import by.leonovich.hibernatecrm.dao.BaseDaoTest;
import by.leonovich.hibernatecrm.dao.Dao;
import by.leonovich.hibernatecrm.mappings.singletable.Student;
import by.leonovich.hibernatecrm.mappings.singletable.University;
import lombok.SneakyThrows;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Created : 08/12/2020 21:55
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
class StudentDaoTest implements BaseDaoTest<Student> {
    private static final Logger LOG = LoggerFactory.getLogger(StudentDaoTest.class);
    private static final MagicList<Student> students = new MagicList<>();

    @Autowired
    private Dao<Student> dao;
    @Autowired
    private Dao<University> universityDao;

    @Test
    @SneakyThrows
    void testSaveCascade() {
        Student student = Student.initWithManyToOne();
        University university = student.getUniversity();
        dao().save(student);
        LOG.debug("Relation saved as well? {}", Objects.nonNull(student.getUniversity().getId()));
        MatcherAssert.assertThat(
            TestConstants.M_SAVE_CASCADE,
            university.getId(),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_UpdateCascade() {
        Student toUpdate = Student.initWithManyToOne();
        University modified = toUpdate.modifyCascade().getUniversity();
        dao().saveOrUpdate(toUpdate);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATED_CASCADE, modified, toUpdate),
            dao().get(toUpdate.getId()).getUniversity(),
            Matchers.equalTo(modified)
        );
    }

    @Test
    @SneakyThrows
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void testDeleteCascade() {
        Student toDelete = Student.initWithManyToOne();
        dao().saveOrUpdate(toDelete);
        Assertions.assertNotNull(toDelete.getId(), TestConstants.M_SAVE);
        University university = toDelete.getUniversity();
        boolean removed = university.expelStudent(toDelete);
        LOG.debug("Student {} expelled from University {}? {}", toDelete.getId(), university.getId(),  removed);

        dao().delete(toDelete);
        Assertions.assertNull(dao().get(toDelete.getId()), String.format(TestConstants.M_DELETE, toDelete));

        Assertions.assertNotNull(
            universityDao.get(university.getId()),
            String.format(TestConstants.M_DELETE_CASCADE_AND_KEEP_RELATION, university, toDelete));
    }

    @Override
    public Dao<Student> dao() {
        return dao;
    }

    @Override
    public MagicList<Student> entities() {
        return students;
    }

    @Override
    public Student generate() {
        return Student.initWithManyToOne();
    }
}
