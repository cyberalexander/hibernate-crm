package by.leonovich.hibernatecrm.dao;

import by.leonovich.hibernatecrm.hibernate.HibernateUtil;
import by.leonovich.hibernatecrm.mappings.singletable.Person;
import by.leonovich.hibernatecrm.common.collection.MagicList;
import by.leonovich.hibernatecrm.common.random.RandomString;
import lombok.SneakyThrows;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PersistenceException;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private static final Dao<Person> dao = new PersonDao();
    private static HibernateUtil hibernate;
    private static final MagicList<Person> all = new MagicList<>();

    @BeforeAll
    static void beforeAll() {
        hibernate = HibernateUtil.getInstance();
        all.addAll(
            Stream.generate(Person::init).limit(10).map(HibernateAbilitiesTest::persistEach).collect(Collectors.toList())
        );
    }

    @Test
    @SneakyThrows
    void testContains() {
        Session session = hibernate.getSession();
        Person notInContext = all.randomEntity();

        MatcherAssert.assertThat("Object should not be associated with session",
            session.contains(notInContext),
            Matchers.is(Boolean.FALSE)
        );
    }

    @Test
    @SneakyThrows
    void testEvict() {
        Session session = hibernate.getSession();
        Long id = all.randomEntity().getId();
        Transaction t = session.beginTransaction();
        Person test = session.get(Person.class, id); //attached to context
        session.evict(test); //removed from context

        MatcherAssert.assertThat("Object expected to be removed from session",
            session.contains(test),
            Matchers.is(Boolean.FALSE)
        );
        t.commit();
    }

    /**
     * Explicitly flushing information from context to database. If entity was changed in context,
     * changes will be stored in database.
     */
    @Test
    @SneakyThrows
    void testFlush() {
        Person detached = all.randomEntity();
        Session session = hibernate.getSession();
        LOG.info("isDirty={}, in memory : {}", session.isDirty(), detached);

        Transaction t = session.beginTransaction();
        Person flushed = session.get(Person.class, detached.getId());
        LOG.info("isDirty={}, from session : {}", session.isDirty(), flushed);
        flushed.setName("TEST_FLUSH_NAME");
        flushed.setSurname("TEST_FLUSH_SURNAME");
        LOG.info("isDirty={}, modified in context : {}", session.isDirty(), flushed);
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
        Person detached = all.randomEntity();
        Session session = hibernate.getSession();
        LOG.info("isDirty={}, in memory : {}", session.isDirty(), detached);

        Transaction t = session.beginTransaction();
        Person refreshed = session.get(Person.class, detached.getId());
        LOG.info("isDirty={}, from session : {}", session.isDirty(), refreshed);
        refreshed.setName("TEST_REFRESH_NAME");
        refreshed.setSurname("TEST_REFRESH_SURNAME");
        LOG.info("isDirty={}, modified in context : {}", session.isDirty(), refreshed);
        session.refresh(refreshed);
        t.commit();
        LOG.info("isDirty={}, after refresh : {}", session.isDirty(), refreshed);

        t = session.beginTransaction();
        Person queried = session.load(Person.class, detached.getId());
        t.commit();

        MatcherAssert.assertThat("Refreshed should be equal to it's state in database",
            refreshed.equals(queried),
            Matchers.is(Boolean.TRUE)
        );
    }

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void testPersist() {
        Person random = all.randomEntity();
        Session session = hibernate.getSession();
        Transaction t = session.beginTransaction();
        LOG.info("isDirty={}, before modify : {}", session.isDirty(), random);

        random = session.get(Person.class, random.getId());
        random.setName("MODIFIED_" + RandomString.NAME.get());
        LOG.info("isDirty={}, after modify : {}", session.isDirty(), random);
        session.persist(random);
        t.commit();

        t = session.beginTransaction();
        Person persisted = session.get(Person.class, random.getId());
        t.commit();

        LOG.info("isDirty={}, persisted : {}", session.isDirty(), persisted);
        Assertions.assertEquals(random.getName(), persisted.getName());
    }

    /**
     * {@link Session#persist} will throw {@link PersistenceException} in case in method passed Entity, which is not
     * associated with session and and it's id parameter is not null. Hibernate will think, this entity already stored
     * in database.
     * Lik in this method we are calling persist for existing in database object, but this object is not attached to the
     * session. Or like in {@link HibernateAbilitiesTest#testPersistThrowException2()} where we creating new object
     * and populating it's id with some value (like it was already stored in database)
     */
    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void testPersistThrowException() {
        Person random = all.randomEntity();
        Session session = hibernate.getSession();

        Transaction t = session.beginTransaction();
        LOG.info("isDirty={}, before modify : {}", session.isDirty(), random);
        random.setName("MODIFIED_" + RandomString.NAME.get());
        LOG.info("isDirty={}, after modify : {}", session.isDirty(), random);

        Assertions.assertThrows(
            PersistenceException.class,
            () -> session.persist(random),
            "Expected [javax.persistence.PersistenceException: org.hibernate.PersistentObjectException: " +
                "detached entity passed to persist]!");

        t.commit();
    }

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void testPersistThrowException2() {
        Person newPerson = new Person();
        newPerson.setId(new Random().nextLong());

        Session session = hibernate.getSession();
        Transaction t = session.beginTransaction();
        LOG.info("isDirty={}, before persist : {}", session.isDirty(), newPerson);

        Assertions.assertThrows(
            PersistenceException.class,
            () -> session.persist(newPerson),
            "Expected [javax.persistence.PersistenceException: org.hibernate.PersistentObjectException: " +
                "detached entity passed to persist]!");

        t.commit();
    }

    @SneakyThrows
    private static Person persistEach(Person person) {
        dao.save(person);
        if (LOG.isInfoEnabled()){
            LOG.info("{}", person);
        }
        return person;
    }
}
