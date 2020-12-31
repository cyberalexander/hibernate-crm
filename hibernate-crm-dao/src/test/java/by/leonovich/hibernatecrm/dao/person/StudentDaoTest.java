package by.leonovich.hibernatecrm.dao.person;

import by.leonovich.hibernatecrm.TestConstants;
import by.leonovich.hibernatecrm.mappings.singletable.Student;
import by.leonovich.hibernatecrm.mappings.singletable.University;
import lombok.SneakyThrows;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Created : 08/12/2020 21:55
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
class StudentDaoTest extends CommonPersonDaoTest {
    protected static final Logger LOG = LoggerFactory.getLogger(StudentDaoTest.class);

    @Test
    @SneakyThrows
    void testPersist() {
        Student student = Student.init();
        dao.persist(student);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_PERSIST, student),
            dao.get(student.getId()),
            Matchers.equalTo(student)
        );
    }

    @Test
    @SneakyThrows
    void testSave() {
        Student student = Student.init();
        MatcherAssert.assertThat(
            TestConstants.M_SAVE,
            dao.save(student),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testSaveCascade() {
        Student student = Student.initWithManyToOne();
        University university = student.getUniversity();
        dao.save(student);
        MatcherAssert.assertThat(
            TestConstants.M_SAVE,
            university.getId(),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_Save() {
        Student student = Student.init();
        dao.saveOrUpdate(student);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_SAVE, student),
            dao.get(student.getId()),
            Matchers.equalTo(student)
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_Update() {
        Student student = Student.init();
        dao.save(student);
        dao.saveOrUpdate(student.modify());
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATE, student),
            dao.get(student.getId()),
            Matchers.equalTo(student)
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_UpdateCascade() {
        Student toUpdate = Student.initWithManyToOne();
        University modified = toUpdate.modifyCascade().getUniversity();
        dao.saveOrUpdate(toUpdate);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATE, modified),
            ((Student) dao.get(toUpdate.getId())).getUniversity(),
            Matchers.equalTo(modified)
        );
    }

    @Test
    @SneakyThrows
    void testGet() {
        Student student = students.randomEntity();
        LOG.info("Random student : {}", student);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_GET, student.getClass().getSimpleName(), student.getId()),
            dao.get(student.getId()),
            Matchers.instanceOf(Student.class)
        );
    }

    @Test
    @SneakyThrows
    void testLoad() {
        Serializable randomIndex = students.randomEntity().getId();
        MatcherAssert.assertThat(
            String.format(TestConstants.M_LOAD, Student.class.getSimpleName(), randomIndex),
            dao.load(randomIndex),
            Matchers.notNullValue()
        );
    }
}
