package by.leonovich.hibernatecrm.dao.meeting;

import by.leonovich.hibernatecrm.dao.Dao;
import by.leonovich.hibernatecrm.dao.MeetingDao;
import by.leonovich.hibernatecrm.dao.person.CommonPersonDaoTest;
import by.leonovich.hibernatecrm.mappings.singletable.Meeting;
import by.leonovich.hibernatecrm.common.collection.MagicList;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static by.leonovich.hibernatecrm.TestConstants.LIMIT;

/**
 * Created : 26/12/2020 15:26
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public class CommonMeetingDaoTest extends CommonPersonDaoTest {
    protected static final MagicList<Meeting> meetings = new MagicList<>();
    protected static final Dao<Meeting> meetingDao = new MeetingDao();

    @BeforeAll
    static void beforeAll() {
        meetings.addAll(
            Stream.generate(Meeting::init)
                .limit(LIMIT)
                .map(CommonMeetingDaoTest::persist)
                .collect(Collectors.toList())
        );
        employees.forEach(emp -> {
            Meeting m = meetings.randomEntity();
            emp.getMeetings().add(m);
            m.getEmployees().add(emp);
            CommonPersonDaoTest.persist(emp);
        });
    }

    @SneakyThrows
    private static Meeting persist(Meeting meeting) {
        meetingDao.saveOrUpdate(meeting);
        LOG.info("{}", meeting);
        return meeting;
    }
}
