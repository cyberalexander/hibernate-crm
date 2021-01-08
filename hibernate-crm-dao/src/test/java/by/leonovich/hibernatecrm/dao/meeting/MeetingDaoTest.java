package by.leonovich.hibernatecrm.dao.meeting;

import by.leonovich.hibernatecrm.TestConstants;
import by.leonovich.hibernatecrm.common.collection.MagicList;
import by.leonovich.hibernatecrm.dao.BaseDaoTest;
import by.leonovich.hibernatecrm.dao.Dao;
import by.leonovich.hibernatecrm.dao.MeetingDao;
import by.leonovich.hibernatecrm.dao.PersonDao;
import by.leonovich.hibernatecrm.hibernate.HibernateUtil;
import by.leonovich.hibernatecrm.mappings.singletable.Employee;
import by.leonovich.hibernatecrm.mappings.singletable.Meeting;
import by.leonovich.hibernatecrm.mappings.singletable.Person;
import lombok.SneakyThrows;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static by.leonovich.hibernatecrm.TestConstants.LIMIT;

/**
 * Created : 26/12/2020 12:58
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
class MeetingDaoTest implements BaseDaoTest<Meeting> {
    private static final Logger LOG = LoggerFactory.getLogger(MeetingDaoTest.class);
    private static final Dao<Person> personDao = new PersonDao();
    private static final Dao<Meeting> meetingDao = new MeetingDao();
    private static final MagicList<Meeting> meetings = new MagicList<>();

    @BeforeAll
    static void beforeAll() {
        meetings.addAll(
            Stream.generate(Meeting::initWithManyToMany)
                .limit(LIMIT)
                .map(MeetingDaoTest::persist)
                .collect(Collectors.toList())
        );
        HibernateUtil.getInstance().closeSession();
    }

    @AfterEach
    void tearDown() {
        //Approach: Session opened in DAO method; session closed here after each @test method execution
        HibernateUtil.getInstance().closeSession();
    }

    @Test
    @SneakyThrows
    void testSaveCascade() {
        Meeting m = Meeting.initWithManyToMany();
        dao().saveOrUpdate(m);
        MatcherAssert.assertThat(
            TestConstants.M_SAVE_CASCADE,
            dao().get(m.getId()).getEmployees(),
            Matchers.equalTo(m.getEmployees())
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_SaveCascade() {
        Meeting meeting = Meeting.initWithManyToMany();
        dao().saveOrUpdate(meeting);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_SAVE_CASCADE, meeting.getEmployees(), meeting),
            dao().get(meeting.getId()).getEmployees().size(),
            Matchers.is(2)
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_UpdateCascade() {
        Meeting meeting = Meeting.initWithManyToMany();
        dao().save(meeting);
        dao().saveOrUpdate(meeting.modifyCascade());
        Set<Employee> modified = meeting.getEmployees();
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATED_CASCADE, modified, meeting),
            dao().get(meeting.getId()).getEmployees(),
            Matchers.equalTo(modified)
        );
    }

    /**
     * 1. Save Meeting & related Employees in DB
     * 2. Assert if Employees were persisted as well
     * 3. Remove reference to Meeting manually, before delete Meeting in order
     * to avoid {@link org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException} (Meetings-to-Employees cascade = save-update)
     * 4. Delete Meeting from DB
     * 5. Assert that Meeting does not exists in database any more
     */
    @Test
    @SneakyThrows
    void testDeleteCascade() {
        Meeting meeting = Meeting.initWithManyToMany();
        Employee relatedEmployee = meeting.getEmployees().iterator().next();

        Assertions.assertNotNull(dao().save(meeting), TestConstants.M_SAVE); //[1]
        meeting.getEmployees().forEach(emp -> Assertions.assertNotNull(emp.getId(), TestConstants.M_SAVE)); //[2]

        //[3]
        for (Employee emp : meeting.getEmployees()) {
            emp.setMeetings(
                emp.getMeetings().stream().filter(m -> !m.equals(meeting)).collect(Collectors.toSet())
            );
            personDao.saveOrUpdate(emp);
        }

        dao().delete(meeting); //[4]

        //[5]
        MatcherAssert.assertThat(String.format(TestConstants.M_DELETE_CASCADE_AND_KEEP_RELATION, relatedEmployee, meeting),
            personDao.get(relatedEmployee.getId()),
            Matchers.notNullValue()
        );
    }

    /**
     * 1. Save Meeting & related Employees in DB
     * 2. Assert if Employees were persisted as well
     * 3. Remove reference to Meeting manually, before delete Meeting in order
     * to avoid {@link org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException} (Meetings-to-Employees cascade = save-update)
     * 4. Delete Meeting from DB
     * 5. Assert that Employees remain persisted in database
     */
    @Test
    @SneakyThrows
    void testDeleteMeetingAndDetachRelatedEmployees() {
        Meeting meeting = Meeting.initWithManyToMany();
        meetingDao.saveOrUpdate(meeting); //[1]

        Assertions.assertNotNull(meeting.getId(), TestConstants.M_SAVE);
        meeting.getEmployees().forEach(emp -> Assertions.assertNotNull(emp.getId(), TestConstants.M_SAVE)); //[2]

        //[3]
        for (Employee emp : meeting.getEmployees()) {
            emp.setMeetings(
                emp.getMeetings().stream().filter(m -> !m.equals(meeting)).collect(Collectors.toSet())
            );
            personDao.saveOrUpdate(emp);
        }

        meetingDao.delete(meeting);//[4]

        //[5]
        for (Employee emp : meeting.getEmployees()) {
            MatcherAssert.assertThat(String.format(TestConstants.M_DELETE_CASCADE_AND_KEEP_RELATION, emp, meeting),
                personDao.get(emp.getId()),
                Matchers.notNullValue()
            );
        }
    }

    @Test
    @SneakyThrows
    void testExpiredColumn() {
        for (Meeting m : meetings) {
            Meeting queried = meetingDao.get(m.getId());
            MatcherAssert.assertThat(
                String.format(TestConstants.M_MEETING_EXPIRED_TEST, queried),
                LocalDateTime.now().isAfter(queried.getMeetingDate()),
                Matchers.equalTo(queried.getExpired())
            );
        }
    }

    @Override
    public Dao<Meeting> dao() {
        return meetingDao;
    }

    @Override
    public MagicList<Meeting> entities() {
        return meetings;
    }

    @SneakyThrows
    private static Meeting persist(Meeting meeting) {
        meetingDao.saveOrUpdate(meeting);
        LOG.info("{}", meeting);
        return meeting;
    }
}
