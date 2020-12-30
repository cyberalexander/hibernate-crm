package by.leonovich.hibernatecrm.dao.university;

import by.leonovich.hibernatecrm.dao.Dao;
import by.leonovich.hibernatecrm.dao.UniversityDao;
import by.leonovich.hibernatecrm.dao.person.CommonPersonDaoTest;
import by.leonovich.hibernatecrm.mappings.singletable.University;
import by.leonovich.hibernatecrm.tools.MagicList;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static by.leonovich.hibernatecrm.TestConstants.LIMIT;

/**
 * Created : 26/12/2020 15:35
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public class CommonUniversityDaoTest extends CommonPersonDaoTest {
    protected static MagicList<University> universities = new MagicList<>();
    protected static final Dao<University> universityDao = new UniversityDao();

    @BeforeAll
    static void beforeAll() {
        universities.addAll(Stream.generate(University::init).limit(LIMIT)
            .map(CommonUniversityDaoTest::persist)
            .collect(Collectors.toList())
        );
        students.forEach(student -> {
            University u = universities.randomEntity();
            student.setUniversity(u);
            u.getStudents().add(student);
            CommonPersonDaoTest.persist(student);
        });
    }


    @SneakyThrows
    private static University persist(University university) {
        universityDao.saveOrUpdate(university);
        LOG.info("{}", university);
        return university;
    }
}
