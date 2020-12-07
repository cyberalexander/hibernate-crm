package by.leonovich.hibernatecrm.dao;

import by.leonovich.hibernatecrm.hibernate.HibernateUtil;
import by.leonovich.hibernatecrm.mappings.singletable.Person;
import by.leonovich.hibernatecrm.tools.RandomStrings;
import lombok.SneakyThrows;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static by.leonovich.hibernatecrm.tools.RandomStrings.NAME;
import static by.leonovich.hibernatecrm.tools.RandomStrings.SURNAME;

/**
 * Created : 06/12/2020 20:47
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
class HibernateAbilitiesTest {
    private static final Logger LOG = LoggerFactory.getLogger(HibernateAbilitiesTest.class);

    private static HibernateUtil hibernate;

    private static final Dao<Person> dao = new BaseDao<>();
    private static List<Person> all;
    private static Random r;

    @BeforeAll
    static void beforeAll() {
        hibernate = HibernateUtil.getInstance();
        r = new Random();

        all = Stream.generate(Person::new)
            .limit(10)
            .map(person -> {
                person.setName(new RandomStrings(NAME).get());
                person.setSurname(new RandomStrings(SURNAME).get());
                person.setAge(new Random().nextInt(99 - 1) + 1);
                try {
                    dao.saveOrUpdate(person);
                } catch (DaoException e) {
                    throw new RuntimeException(e);
                }
                return person;
            })
            .collect(Collectors.toList());
        LOG.debug("Test data generated : {}", all);
    }

    @Test
    @SneakyThrows
    void testContains() {
        Session session = hibernate.getSession();
        Person notInContext = all.get(r.nextInt(all.size() - 1));

        MatcherAssert.assertThat("Object should not be associated with session",
            session.contains(notInContext),
            Matchers.is(Boolean.FALSE)
        );
    }

    @Test
    @SneakyThrows
    void testEvict() {
        Session session = hibernate.getSession();
        Long id = all.get(r.nextInt(all.size() - 1)).getId();
        Person test = session.get(Person.class, id); //attached to context
        session.evict(test); //removed from context

        MatcherAssert.assertThat("Object expected to be removed from session",
            session.contains(test),
            Matchers.is(Boolean.FALSE)
        );
    }

    /**
     * Explicitly flushing information from context to database. If entity was changed in context,
     * changes will be stored in database.
     */
    @Test
    @SneakyThrows
    void testFlush() {
        Person detached = all.get(r.nextInt(all.size() - 1));
        Session session = hibernate.getSession();
        LOG.debug("isDirty={}, in memory : {}", session.isDirty(), detached);

        Transaction t = session.beginTransaction();
        Person flushed = session.get(Person.class, detached.getId());
        LOG.debug("isDirty={}, from session : {}", session.isDirty(), flushed);
        flushed.setName("TEST_FLUSH_NAME");
        flushed.setSurname("TEST_FLUSH_SURNAME");
        LOG.debug("isDirty={}, modified in context : {}", session.isDirty(), flushed);
        session.flush();
        t.commit();

        t = session.beginTransaction();
        Person queried = session.load(Person.class, detached.getId());
        t.commit();
        MatcherAssert.assertThat("Flushed changes expected to be persisted in database",
            flushed.equals(queried),
            Matchers.is(Boolean.TRUE)
        );
        MatcherAssert.assertThat("Detached object expected to be different from flushed after change",
            queried.equals(detached),
            Matchers.is(Boolean.FALSE)
        );
    }

    /**
     * Refresh information about entity(ies) in context with actual data from database. If entity was changed
     * in context, refresh will erase those changes.
     */
    @Test
    @SneakyThrows
    void testRefresh() {
        Person detached = all.get(r.nextInt(all.size() - 1));
        Session session = hibernate.getSession();

        Transaction t = session.beginTransaction();
        Person refreshed = session.get(Person.class, detached.getId());
        LOG.debug("isDirty={}, from session : {}", session.isDirty(), refreshed);
        refreshed.setName("TEST_REFRESH_NAME");
        refreshed.setSurname("TEST_REFRESH_SURNAME");
        LOG.debug("isDirty={}, modified in context : {}", session.isDirty(), refreshed);
        session.refresh(refreshed);
        t.commit();
        LOG.debug("isDirty={}, after refresh : {}", session.isDirty(), refreshed);

        t = session.beginTransaction();
        Person queried = session.load(Person.class, detached.getId());
        t.commit();

        MatcherAssert.assertThat("Refreshed should be equal to it's state in database",
            refreshed.equals(queried),
            Matchers.is(Boolean.TRUE)
        );
    }
}
