package by.leonovich.hibernatecrm.dao.university;

import by.leonovich.hibernatecrm.TestConstants;
import by.leonovich.hibernatecrm.common.collection.MagicList;
import by.leonovich.hibernatecrm.dao.BaseDaoTest;
import by.leonovich.hibernatecrm.dao.Dao;
import by.leonovich.hibernatecrm.dao.PersonDao;
import by.leonovich.hibernatecrm.dao.UniversityDao;
import by.leonovich.hibernatecrm.hibernate.HibernateUtil;
import by.leonovich.hibernatecrm.mappings.singletable.Person;
import by.leonovich.hibernatecrm.mappings.singletable.Student;
import by.leonovich.hibernatecrm.mappings.singletable.University;
import lombok.SneakyThrows;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created : 13/12/2020 20:24
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
class UniversityDaoTest implements BaseDaoTest<University> {
    protected static final Logger LOG = LoggerFactory.getLogger(UniversityDaoTest.class);
    protected static final Dao<Person> personDao = new PersonDao();
    protected static final Dao<University> universityDao = new UniversityDao();
    protected static MagicList<University> universities = new MagicList<>();

    @BeforeAll
    static void beforeAll() {
        universities.addAll(Stream.generate(University::initWithOneToMany).limit(TestConstants.LIMIT)
            .map(UniversityDaoTest::persist)
            .collect(Collectors.toList())
        );
        HibernateUtil.getInstance().closeSession();
    }

    @AfterEach
    void tearDown() {
        HibernateUtil.getInstance().closeSession();
    }

    @Test
    @SneakyThrows
    void testSaveCascade() {
        University u = University.initWithOneToMany();
        dao().save(u);
        LOG.debug("Relation saved as well? {}", Objects.nonNull(u.getStudents().iterator().next().getId()));
        MatcherAssert.assertThat(
            TestConstants.M_SAVE_CASCADE,
            dao().get(u.getId()).getStudents().iterator().next().getId(),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_SaveCascade() {
        University u = University.initWithOneToMany();
        universityDao.saveOrUpdate(u);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_SAVE_CASCADE, u.getStudents(), u),
            daoGet(u.getId()).getStudents().size(),
            Matchers.is(2)
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_UpdateCascade() {
        University toUpdate = universities.randomEntity();
        Set<Student> updatedStudents = toUpdate.modifyCascade().getStudents();
        universityDao.saveOrUpdate(toUpdate);
        Set<Student> queried = universityDao.get(toUpdate.getId()).getStudents();
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATED_CASCADE, updatedStudents, toUpdate),
            queried,
            Matchers.equalTo(updatedStudents)
        );
    }

    /**
     * cascade="all-delete-orphan" - when deleting University, all related Student should be deleted by hibernate as well.
     */
    @Test
    @SneakyThrows
    void testDeleteCascade() {
        University toDelete = University.initWithOneToMany();
        universityDao.save(toDelete);
        Assertions.assertNotNull(toDelete.getId(), TestConstants.M_SAVE);
        Student relatedStudent = toDelete.getStudents().iterator().next();

        universityDao.delete(toDelete);

        MatcherAssert.assertThat(String.format(TestConstants.M_DELETE_CASCADE, relatedStudent, toDelete),
            personDao.get(relatedStudent.getId()),
            Matchers.nullValue()
        );
    }

    @Test
    @SneakyThrows
    void testDeleteOrphan() {
        University u = University.initWithOneToMany();
        universityDao.save(u);
        Assertions.assertNotNull(u.getId(), TestConstants.M_SAVE);
        u = universityDao.get(u.getId()); //Important to load entity in context to successfully delete orphan

        Student orphan = u.getStudents().iterator().next();
        boolean removed = u.expelStudent(orphan);
        LOG.debug("Student {} expelled from University {}? {}",orphan.getId(), u.getId(),  removed);
        universityDao.saveOrUpdate(u);

        MatcherAssert.assertThat(String.format(TestConstants.M_DELETE_ORPHAN, orphan, u),
            personDao.get(orphan.getId()),
            Matchers.nullValue()
        );
    }

    @Override
    public Dao<University> dao() {
        return universityDao;
    }

    @Override
    public MagicList<University> entities() {
        return universities;
    }

    @SneakyThrows
    private University daoGet(Serializable id) {
        return universityDao.get(id);
    }

    @SneakyThrows
    private static University persist(University university) {
        universityDao.saveOrUpdate(university);
        LOG.info("{}", university);
        return university;
    }
}
