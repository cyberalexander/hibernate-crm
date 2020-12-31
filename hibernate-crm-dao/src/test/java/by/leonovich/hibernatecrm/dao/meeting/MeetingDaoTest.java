package by.leonovich.hibernatecrm.dao.meeting;

import by.leonovich.hibernatecrm.TestConstants;
import by.leonovich.hibernatecrm.mappings.singletable.Employee;
import by.leonovich.hibernatecrm.mappings.singletable.Meeting;
import lombok.SneakyThrows;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created : 26/12/2020 12:58
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
class MeetingDaoTest extends CommonMeetingDaoTest {
    protected static final Logger LOG = LoggerFactory.getLogger(MeetingDaoTest.class);

    @Test
    @SneakyThrows
    void testPersist() {
        Meeting meeting = Meeting.init();
        meetingDao.persist(meeting);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_PERSIST, meeting),
            meetingDao.get(meeting.getId()),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testSave() {
        MatcherAssert.assertThat(
            TestConstants.M_SAVE,
            meetingDao.save(Meeting.init()),
            Matchers.notNullValue()
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_Save() {
        Meeting meeting = Meeting.init();
        meetingDao.saveOrUpdate(meeting);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_SAVE, meeting),
            meetingDao.get(meeting.getId()),
            Matchers.equalTo(meeting)
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_SaveCascade() {
        Meeting meeting = Meeting.initWithManyToMany();
        meetingDao.saveOrUpdate(meeting);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_SAVE_CASCADE, meeting.getEmployees()),
            meetingDao.get(meeting.getId()).getEmployees().size(),
            Matchers.is(3)
        );
    }

    @Test
    @SneakyThrows
    void testSaveOrUpdate_Update() {
        Meeting modified = meetings.randomEntity().modify();
        meetingDao.saveOrUpdate(modified);
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATE, modified),
            meetingDao.get(modified.getId()),
            Matchers.equalTo(modified)
        );
    }

    /**
     * 1. After persisting Meeting and new Employees to database, it's important to execute GET operation, to load
     * populate Employees IDs. Or else [2] invocation will try to persist same employees again and following exception
     * will be thrown:
     * <code>
     * Caused by: org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException: Unique index or primary key violation:
     * "PUBLIC.PRIMARY_KEY_87 ON PUBLIC.T_EMPLOYEE_MEETING(F_EMPLOYEE_ID, F_MEETING_ID) VALUES 1"; SQL statement:
     * insert into T_EMPLOYEE_MEETING (F_EMPLOYEE_ID, F_MEETING_ID) values (?, ?) [23505-200]
     * </code>
     */
    @Test
    @SneakyThrows
    void testSaveOrUpdate_UpdateCascade() {
        Meeting toUpdate = Meeting.initWithManyToMany();
        meetingDao.saveOrUpdate(toUpdate);
        toUpdate = meetingDao.get(toUpdate.getId()); //[1]
        Set<Employee> updated = toUpdate.modifyCascade().getEmployees();
        meetingDao.saveOrUpdate(toUpdate); //[2]
        Set<Employee> queried = meetingDao.get(toUpdate.getId()).getEmployees();
        MatcherAssert.assertThat(
            String.format(TestConstants.M_SAVE_OR_UPDATE_UPDATE, updated),
            queried,
            Matchers.equalTo(updated)
        );
    }

    @Test
    @SneakyThrows
    void testGet() {
        Meeting meeting = meetings.randomEntity();
        MatcherAssert.assertThat(
            String.format(TestConstants.M_GET, meeting.getClass().getSimpleName(), meeting.getId()),
            meetingDao.get(meeting.getId()),
            Matchers.instanceOf(Meeting.class)
        );
    }

    @Test
    @SneakyThrows
    void testGetWhenNotExists() {
        Serializable index = meetings.lastElement().incrementIdAndGet();
        MatcherAssert.assertThat(
            String.format(TestConstants.M_GET_NOT_EXISTS, index),
            meetingDao.get(index),
            Matchers.nullValue()
        );
    }

    @Test
    @SneakyThrows
    void testLoad() {
        Serializable index = meetings.randomEntity().getId();
        MatcherAssert.assertThat(
            String.format(TestConstants.M_LOAD, Meeting.class.getSimpleName(), index),
            meetingDao.load(index),
            Matchers.notNullValue()
        );
    }

    /**
     * Method {@link Session#load} is different from {@link Session#get}
     * In case of "get", when object not exists in database, hibernate returns null. When in case of "load"
     * hibernate will not query object immediately but will just return proxy. And when we try to access any property
     * of that proxy, hibernate will immediately execute select to database and throw {@link ObjectNotFoundException}
     * if object won't be found there.
     */
    @Test
    @SneakyThrows
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void testLoadWhenNotExists() {
        Serializable index = meetings.lastElement().incrementIdAndGet();
        Meeting loaded = meetingDao.load(index);
        Assertions.assertThrows(
            ObjectNotFoundException.class,
            loaded::getSubject,
            TestConstants.M_LOAD_EXCEPTION);
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
    void testDelete() {
        Meeting meeting = Meeting.initWithManyToMany();
        meetingDao.saveOrUpdate(meeting); //[1]

        Assertions.assertNotNull(meeting.getId(), TestConstants.M_SAVE);
        meeting.getEmployees().forEach(emp -> Assertions.assertNotNull(emp.getId(), TestConstants.M_SAVE)); //[2]

        //[3]
        for (Employee emp : meeting.getEmployees()) {
            emp.setMeetings(
                emp.getMeetings().stream().filter(m -> !m.equals(meeting)).collect(Collectors.toSet())
            );
            dao.saveOrUpdate(emp);
        }

        meetingDao.delete(meeting); //[4]

        //[5]
        MatcherAssert.assertThat(String.format(TestConstants.M_DELETE, meeting),
            meetingDao.get(meeting.getId()),
            Matchers.nullValue()
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
            dao.saveOrUpdate(emp);
        }

        meetingDao.delete(meeting);//[4]

        //[5]
        for (Employee emp : meeting.getEmployees()) {
            MatcherAssert.assertThat(String.format(TestConstants.M_DELETE_RELATION, emp, meeting),
                dao.get(emp.getId()),
                Matchers.notNullValue()
            );
        }
    }

    @Test
    @SneakyThrows
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void testGetAll() {
        List<Serializable> queried = meetingDao.getAll(Meeting.class).stream()
            .map(Meeting::getId)
            .collect(Collectors.toList());
        meetings.stream().map(Meeting::getId).forEach(meetingId ->
            MatcherAssert.assertThat(
                String.format(TestConstants.M_GET_ALL, Meeting.class.getSimpleName(), meetingId),
                queried.contains(meetingId),
                Matchers.is(Boolean.TRUE)
            ));
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
}
