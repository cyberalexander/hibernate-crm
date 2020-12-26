package by.leonovich.hibernatecrm.dao.person;

import by.leonovich.hibernatecrm.TestConstants;
import by.leonovich.hibernatecrm.mappings.singletable.Student;
import by.leonovich.hibernatecrm.tools.RandomString;
import lombok.SneakyThrows;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Created : 08/12/2020 21:55
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
class StudentDaoTest extends AbstractPersonDaoTest {

    @Test
    @SneakyThrows
    void testPersist() {
        Student student = Student.init();
        student.setUniversity(universities.randomEntity());
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
        student.setUniversity(universities.randomEntity());
        MatcherAssert.assertThat(
            TestConstants.M_SAVE,
            dao.save(student),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdateSave() {
        Student toSave = Student.init();
        toSave.setUniversity(universities.randomEntity());
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
        Student toUpdate = students.randomEntity();
        toUpdate.setFaculty("UPDATE_" + RandomString.FACULTY.get() + "_" + toUpdate.getId());
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
        Student randStudent = students.randomEntity();
        LOG.info("{}", randStudent);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_GET, randStudent.getClass().getSimpleName(), randStudent.getId()),
            dao.get(randStudent.getId()),
            Matchers.instanceOf(Student.class)
        );
    }
}
